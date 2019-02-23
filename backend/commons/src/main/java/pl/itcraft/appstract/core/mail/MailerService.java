package pl.itcraft.appstract.core.mail;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import pl.itcraft.appstract.core.mail.MailDto.Attachment;
import pl.itcraft.appstract.core.mail.MailDto.Inline;

@Service
public class MailerService {
	private static final Logger logger = LoggerFactory.getLogger(MailerService.class);
	
	private static final String BODY_LAYOUT_TEMPLATE = "_layout.html";
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${app.mail.from}")
	private String mailFrom;
	
	@Autowired
	private Configuration fmConfig;
	
	public void sendMailMessage(MailDto mail) {
		logger.info("Sending e-mail [{}] to {} ({})", mail.getSubject(), mail.getRecipients().get(0), mail.getRecipients().size());
		MimeMessage message = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			message.setSubject(mail.getSubject());
			helper.setFrom(mailFrom);
			for (String rcpt : mail.getRecipients()) {
				helper.addTo(rcpt);
			}
			helper.setText(getBodyFromTemplate(mail), mail.isHtml());
			for (Attachment a : mail.getAttachments()) {
				helper.addAttachment(a.getAttachmentFilename(), a.getInputStreamSource(), a.getContentType());
			}
			if (mail.getLogo() != null) {
				try {
					InputStream is = getClass().getResourceAsStream(mail.getLogo());
					helper.addInline("logo", new ByteArrayResource(IOUtils.toByteArray(is)), "image/png");
				} catch (IOException e) {
					logger.error("Could not attach inline logo in email", e);
				}
			}
			for (Inline i : mail.getInlines()) {
				helper.addInline(i.getContendId(), i.getInputStreamSource(), i.getContentType());
			}
			mailSender.send(message);
		} catch (Exception e) {
			logger.error("", e);
		}
	}
	
	private String getBodyFromTemplate(MailDto mail) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {
		StringBuffer content = new StringBuffer();
		
		Map<String, Object> model = mail.getModel();
		model.put("bodyTemplate", mail.getBodyTemplate());
		content.append(FreeMarkerTemplateUtils.processTemplateIntoString(fmConfig.getTemplate(BODY_LAYOUT_TEMPLATE), mail.getModel()));
		return content.toString();
	}
}