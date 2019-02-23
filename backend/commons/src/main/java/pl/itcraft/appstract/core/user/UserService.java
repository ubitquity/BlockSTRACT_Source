package pl.itcraft.appstract.core.user;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import pl.itcraft.appstract.core.BaseAppModuleConfig;
import pl.itcraft.appstract.core.CoreConstants;
import pl.itcraft.appstract.core.api.auth.AuthenticationResultDto;
import pl.itcraft.appstract.core.api.error.ApiException;
import pl.itcraft.appstract.core.api.error.RC;
import pl.itcraft.appstract.core.dto.UserDto;
import pl.itcraft.appstract.core.entities.ApiLoginEntry;
import pl.itcraft.appstract.core.entities.UploadedFile;
import pl.itcraft.appstract.core.entities.User;
import pl.itcraft.appstract.core.entities.UserArchivePassword;
import pl.itcraft.appstract.core.enums.UserRole;
import pl.itcraft.appstract.core.file.UploadedFileDescriptor;
import pl.itcraft.appstract.core.file.UploadedFilesWriterService;

@Service
public class UserService {
	
	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private BaseAppModuleConfig moduleConfig;

	@Autowired
	private UserBeanMapper userBeanMapper;
	
	@Autowired
	private UploadedFilesWriterService filesService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public User findById(Long id) {
		String q = "SELECT u FROM User u WHERE u.id = :uid AND u.deleted = FALSE AND u.role != :system";
		List<User> list = em.createQuery(q, User.class)
				.setParameter("uid", id)
				.setParameter("system", UserRole.SYSTEM)
				.getResultList();
		return list.isEmpty() ? null : list.get(0);
	}
	
	@Transactional(readOnly = true)
	public User findForEmailAuthentication(String email) {
		if (StringUtils.isBlank(email)) {
			return null;
		}
		return findForEmailAuthenticationImpl(email, LockModeType.NONE);
	}

	@Transactional(readOnly = true)
	public User findForTokenAuthentication(String accessToken) {
		String q = "SELECT u FROM ApiLoginEntry e JOIN e.user u"
				+ " WHERE e.accessToken = :accessToken AND e.logoutTime IS NULL AND e.tokenExpirationDate > :now";
		TypedQuery<User> query = em.createQuery(q, User.class);
		query.setParameter("accessToken", accessToken);
		query.setParameter("now", new Date());
		query.setMaxResults(1);
		List<User> entries = query.getResultList();
		return entries.isEmpty() ? null : entries.get(0);
	}
	
	@Transactional
	public void extendTokenExpiration(String accessToken) {
		DateTime now = new DateTime();
		em.createQuery("UPDATE ApiLoginEntry SET tokenExpirationDate = :date WHERE accessToken = :token")
			.setParameter("token", accessToken)
			.setParameter("date", now.plusMinutes(moduleConfig.getAuthTokenTtlMinutes()).toDate())
			.executeUpdate();
	}

	@Transactional
	public void onBadCredentialsError(String username) {
		User user = findForEmailAuthenticationImpl(username, LockModeType.PESSIMISTIC_WRITE);
		try {
			Thread.sleep(2000); // Make brute-force harder
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		LOG.warn("Incorrect login attempt for: {}, {}", username, user);
		if (user != null) {
			user.incrementIncorrectLoginsCount( moduleConfig.getMaxLoginFailures() );
			if (user.isAccountLocked()) {
				// TODO wyslac e-mail do usera + admina (?)
				LOG.warn("Account '{}' locked after {} failed login attempts", user, user.getIncorrectLoginsCount());
			}
		}
	}
	
	public List<UserArchivePassword> getUserLastPasswords(User user) {
		String query = "SELECT DISTINCT uap FROM UserArchivePassword uap JOIN FETCH uap.user u WHERE u.id = :uid ORDER BY uap.dateAdded DESC";
		return em.createQuery(query, UserArchivePassword.class)
			.setParameter("uid",  user.getId())
			.setMaxResults(CoreConstants.PASSWORD_HISTORY_SIZE - 1)
			.getResultList();
	}
	
	@Transactional
	public User changeUserPassword(Long userId, String newPassword, boolean needsPasswordReset) {
		User user = findById(userId);
		Date now = new Date();
		UserArchivePassword archivePassword = new UserArchivePassword(user, now, user.getPassword());
		em.persist(archivePassword);

		user.setPassword(passwordEncoder.encode(newPassword));
		user.setNeedsPasswordReset(needsPasswordReset);
		if (CoreConstants.PASSWORD_EXPIRATION_TIME_IN_DAYS > 0) {
			user.setPasswordExpirationDate(DateUtils.addDays(now, CoreConstants.PASSWORD_EXPIRATION_TIME_IN_DAYS));
		} else {
			user.setPasswordExpirationDate(null);
		}
		user.setIncorrectLoginsCount(0);
		return user;
	}
	
	@Transactional
	public User setUserPassword(Long userId, String password, boolean needsPasswordReset) {
		User user = findById(userId);
		setUserPassword(user, password);
		user.setNeedsPasswordReset(needsPasswordReset);
		if (CoreConstants.PASSWORD_EXPIRATION_TIME_IN_DAYS > 0) {
			user.setPasswordExpirationDate(
				DateUtils.addDays(new Date(), CoreConstants.PASSWORD_EXPIRATION_TIME_IN_DAYS)
			);
		} else {
			user.setPasswordExpirationDate(null);
		}
		user.setIncorrectLoginsCount(0);
		return user;
	}
	
	@Transactional
	public User setUserPassword(User user, String password) {
		user.setPassword(passwordEncoder.encode(password));
		return user;
	}

	@Transactional
	public UploadedFile uploadAvatar(Long userId, MultipartFile avatar) {
		User user = em.find(User.class, userId);
		UploadedFile currentAvatar = user.getAvatar();
		if (currentAvatar != null) {
			currentAvatar.markAsToDelete();
			user.setAvatar(null);
		}
		UploadedFile newAvatar = null;
		if (avatar != null && !avatar.isEmpty()) {
			UploadedFileDescriptor fd = UploadedFileDescriptor.create().withMultipartFile(avatar);
			newAvatar = filesService.uploadFile(fd);
			user.setAvatar( newAvatar );
		}
		return newAvatar;
	}

	public UserDto getUserProfile(Long userId) {
		User currentUser = findByIdRequiredActive(userId);
		UserDto dto = userBeanMapper.map(currentUser);		
		return dto;
	}

	public User findByIdRequiredActive(Long userId) {
		User u = findByIdRequired(userId);
		if (!u.isEnabled()) {
			throw new ApiException(RC.RESOURCE_NOT_FOUND);
		}
		return u;
	}

	private User findByIdRequired(Long id) {
		User u = findById(id);
		if (u == null) {
			throw new ApiException(RC.RESOURCE_NOT_FOUND, User.class.getSimpleName());
		}
		return u;
	}

	public List<UserArchivePassword> getUserLastPasswords(User user, Integer limit) {
		String query = "SELECT DISTINCT uap FROM UserArchivePassword uap JOIN FETCH uap.user u WHERE u.id = :uid ORDER BY uap.dateAdded DESC";
		TypedQuery<UserArchivePassword> tq = em.createQuery(query, UserArchivePassword.class);
		tq.setParameter("uid", user.getId());
		tq.setMaxResults(limit - 1);
		return tq.getResultList();
	}

	public boolean isEmailUnique(String email, Long excludeId) {
		return em.createQuery("SELECT u.id FROM User u WHERE LOWER(u.email) = :email AND u.id <> :uid")
			.setMaxResults(1)
			.setParameter("email", email.toLowerCase())
			.setParameter("uid", excludeId)
			.getResultList()
			.isEmpty();
	}

	@Transactional
	public AuthenticationResultDto login(User user, String remoteHost, String userAgent) {
		DateTime now = new DateTime();
		Date tokenExpiration = now.plusMinutes(moduleConfig.getAuthTokenTtlMinutes()).toDate();
		
		ApiLoginEntry entry = new ApiLoginEntry(now.toDate(), user, tokenExpiration);
		entry.setRemoteHost(remoteHost);
		entry.setUserAgent(userAgent);
		em.persist(entry);
		
		em.createQuery("UPDATE User SET lastLoginAt = :date, incorrectLoginsCount = 0 WHERE id = :id")
			.setParameter("date", now.toDate())
			.setParameter("id", user.getId())
			.executeUpdate();
		
		AuthenticationResultDto dto = new AuthenticationResultDto();
		dto.setUser(userBeanMapper.map(user));
		dto.setAccessToken(entry.getAccessToken());
		return dto;
	}
	
	@Transactional
	public void logout(String accessToken) {
		em.createQuery("UPDATE ApiLoginEntry SET logoutTime = :logoutTime, registrationId = NULL WHERE accessToken = :token")
			.setParameter("logoutTime", new Date())
			.setParameter("token", accessToken)
			.executeUpdate();
	}

	@Transactional
	public void setRegistrationId(String accessToken, String registrationId) {
		// Clear Registration Id Duplicates
		em.createQuery("UPDATE ApiLoginEntry SET registrationId = NULL WHERE registrationId = :rid")
			.setParameter("rid", registrationId)
			.executeUpdate();
		
		// Save Id in current login
		em.createQuery("UPDATE ApiLoginEntry SET registrationId = :rid WHERE accessToken = :token")
		.setParameter("rid", registrationId)
		.setParameter("token", accessToken)
		.executeUpdate();
	}

	@Transactional
	public int logoutExpired() {
		return em.createQuery("UPDATE ApiLoginEntry e SET e.logoutTime = NOW(), e.registrationId = NULL "
				+ "WHERE e.logoutTime IS NULL AND e.tokenExpirationDate <= NOW()")
		.executeUpdate();
	}
	
	public User findByEmail(String email) {
		TypedQuery<User> query = em
				.createQuery("SELECT u FROM User u WHERE LOWER(u.email) = :email AND u.deleted = FALSE", User.class)
				.setParameter("email", email.toLowerCase());
		List<User> users = query.getResultList();
		return !users.isEmpty() ? users.get(0) : null;
	}

	private User findForEmailAuthenticationImpl(String username, LockModeType lockMode) {
		// Wystarczy ze deleted != false
		// Reszte (enabled, locked) sprawdzi springowy UserDetailsChecker
		String q = "SELECT u FROM User u WHERE LOWER(u.email) = :username AND u.deleted = FALSE";
		List<User> result = em.createQuery(q, User.class)
			.setParameter("username", username.toLowerCase())
			.setLockMode(lockMode)
			.setMaxResults(1)
			.getResultList();
		if (result.isEmpty()) {
			return null;
		}
		return result.get(0);
	}
}
