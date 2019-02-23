package pl.itcraft.appstract.admin.abstractor.signup;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.itcraft.appstract.commons.users.UserRepository;
import pl.itcraft.appstract.commons.utils.StringGenerator;
import pl.itcraft.appstract.core.AppModule;
import pl.itcraft.appstract.core.BaseAppModuleConfig;
import pl.itcraft.appstract.core.api.error.ApiException;
import pl.itcraft.appstract.core.api.error.RC;
import pl.itcraft.appstract.core.entities.User;
import pl.itcraft.appstract.core.enums.UserRole;
import pl.itcraft.appstract.core.mail.MailDto;
import pl.itcraft.appstract.core.mail.MailerService;
import pl.itcraft.appstract.core.utils.UtilsBean;

import java.util.Locale;
import java.util.Optional;


@Service
public class AbstractorSignupService {

	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UtilsBean utilsBean;

	@Autowired
	private BaseAppModuleConfig appModuleConfig;

	@Autowired
	private MailerService mailerService;

	@Autowired
	UserRepository userRepository;
	
	@Transactional
	public User signupAbstractor(AbstractorSignupDto dto) {
		String pwd = passwordEncoder.encode( dto.getPassword() );
		
		User user = new User(UserRole.ABSTRACTOR, pwd, dto.getEmail(), dto.getFirstName(), dto.getLastName());
		user.setCompanyName(dto.getCompanyName());
		user.setPhoneNumber(dto.getPhoneNumber());
		user.setWeeklyHoursAvailability(dto.getWeeklyHoursAvailability());

		user.setAccountActivated(false);
		user.setAccountLocked(false);
		user.setEnabled(false);
		user.setNotifications(true);
		String token = StringGenerator.generate(20, 30);
		user.setActivationToken(token);
		em.persist(user);
		this.sendEmailWithinActivationLink(utilsBean.getCurrentLocale(), dto.getEmail(), "/activate", token);
		
		return user;
	}

	@Async("APP_DEFAULT_EXECUTOR")
	public void sendEmailWithinActivationLink(Locale locale, String email, String relPath, String passwordResetToken) {
		String subject = utilsBean.getMessage(locale, "mail.activate_abstractor.title");
		String message = utilsBean.getMessage(locale, "mail.activate_abstractor.content");
		String buttonText = utilsBean.getMessage(locale, "mail.activate_abstractor.button");
		String ignoreIfMistake = utilsBean.getMessage(locale, "mail.activate_abstractor.ignore");
		String buttonLink = appModuleConfig.getFrontUrl(AppModule.admin) + relPath + "/" + passwordResetToken;

		MailDto mailDto = new MailDto()
				.withBodyTemplate("activate-user-account.html")
				.asHtml(true)
				.withSubject(subject)
				.withRecipient(email)
				.withModel("TEXT_CLICK_BUTTON", message)
				.withModel("URL", buttonLink)
				.withModel("TEXT_LINK", buttonText)
				.withModel("TEXT_IGNORE_IF_MISSTAKE", ignoreIfMistake);

		mailerService.sendMailMessage(mailDto);
	}

	void activate(String token) {
		Optional<User> userFound = userRepository.findFirstByActivationToken(token);
		if(!userFound.isPresent()) {
			throw new ApiException(RC.NOT_FOUND, "Invalid token");
		}
		User user = userFound.get();
		if(user.isAccountActivated()) {
			throw new ApiException(RC.VALIDATION_ERROR, "User account is already activated");
		}
		user.setAccountActivated(true);
		userRepository.save(user);
	}

}
