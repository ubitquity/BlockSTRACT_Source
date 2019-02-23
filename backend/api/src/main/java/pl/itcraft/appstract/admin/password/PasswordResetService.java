package pl.itcraft.appstract.admin.password;

import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.itcraft.appstract.core.AppModule;
import pl.itcraft.appstract.core.BaseAppModuleConfig;
import pl.itcraft.appstract.core.CoreConstants;
import pl.itcraft.appstract.core.api.auth.AccountErrorType;
import pl.itcraft.appstract.core.api.error.ApiException;
import pl.itcraft.appstract.core.api.error.BusinessException;
import pl.itcraft.appstract.core.api.error.RC;
import pl.itcraft.appstract.core.entities.PasswordReset;
import pl.itcraft.appstract.core.entities.User;
import pl.itcraft.appstract.core.mail.MailDto;
import pl.itcraft.appstract.core.mail.MailerService;
import pl.itcraft.appstract.core.user.UserService;
import pl.itcraft.appstract.core.utils.UtilsBean;

@Service
public class PasswordResetService {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private UserService userService;

	@Autowired
	private MailerService mailerService;

	@Autowired
	private ResetPasswordMapper resetPasswordMapper;
	
	@Autowired
	private UtilsBean utilsBean;
	
	@Autowired
	private BaseAppModuleConfig appModuleConfig;

	@Transactional
	public String reset(ResetPasswordDto dto) {
		User user = userService.findByEmail(dto.getEmail());
		if (user == null) {
			throw new ApiException(RC.RESOURCE_NOT_FOUND);
		}
		if(!user.isAccountActivated()) {
			throw new BusinessException( AccountErrorType.NOT_ACTIVATED );
		}
		if(!user.isEnabled()) {
			throw new BusinessException( AccountErrorType.DISABLED );
		}
		PasswordReset pr = resetPasswordMapper.createPasswordReset(user);
		em.persist(pr);
		return pr.getToken();
	}

	@Async("APP_DEFAULT_EXECUTOR")
	public void sendEmailWithinResetLink(Locale locale, String email, String relPath, String passwordResetToken) {
		String subject = utilsBean.getMessage(locale, "mail.password_reset.title");
		String message = utilsBean.getMessage(locale, "mail.password_reset.content");
		String buttonText = utilsBean.getMessage(locale, "mail.password_reset.button");
		String buttonLink = appModuleConfig.getFrontUrl(AppModule.admin) + relPath + "/" + passwordResetToken;
		
		MailDto mailDto = new MailDto()
			.withBodyTemplate("reset-password.html")
			.asHtml(true)
			.withSubject(subject)
			.withRecipient(email)
			.withModel("message", message)
			.withModel("resetLinkUrl", buttonLink)
			.withModel("resetLinkText", buttonText);
		
		mailerService.sendMailMessage(mailDto);
	}

	@Transactional
	public void deleteUserTokens(User user) {
		em.createQuery("DELETE FROM PasswordReset pr WHERE pr.user = :user").setParameter("user", user).executeUpdate();
	}

	public PasswordReset findByToken(String token) {
		List<PasswordReset> result = em
				.createQuery("SELECT pr FROM PasswordReset pr WHERE pr.token = :token", PasswordReset.class)
				.setParameter("token", token).getResultList();
		if (!result.isEmpty()) {
			return result.get(0);
		}
		return null;
	}

	@Transactional
	public void resetPassword(User user, String newPassword) throws Exception {
		userService.changeUserPassword(user.getId(), newPassword, false);
		if (!user.isAccountActivated()) {
			user.activate(user.getActivationToken());
		}
		deleteUserTokens(user);
	}
	
	@Transactional
	public int deleteExpiredTokens() {
		return em.createQuery("DELETE FROM PasswordReset pr WHERE pr.createdAt <= :validDate")
			.setParameter("validDate", new DateTime().minusSeconds(CoreConstants.PASSWORD_TOKEN_RESET_VALIDITY).toDate())
			.executeUpdate();
	}

}
