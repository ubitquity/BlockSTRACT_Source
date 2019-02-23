package pl.itcraft.appstract.core.mail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.InputStreamSource;

public class MailDto {
	private List<String> recipients = new ArrayList<>();
	private String subject;
	private String bodyTemplate;
	private boolean html;
	private List<Attachment> attachments = new ArrayList<>();
	private List<Inline> inlines = new ArrayList<>();
	private Map<String,Object> model = new HashMap<>();
	private String logo;
	
	public MailDto withRecipient(String recipient) {
		recipients.add(recipient);
		return this;
	}
	
	public MailDto withRecipients(List<String> recipients) {
		recipients.forEach(item -> this.recipients.add(item));
		return this;
	}
	
	public MailDto withModel(String key, Object value) {
		model.put(key, value);
		return this;
	}
	
	public MailDto withSubject(String subject) {
		this.subject = subject;
		return this;
	}
	
	public MailDto withBodyTemplate(String bodyTemplate) {
		this.bodyTemplate = bodyTemplate;
		return this;
	}
	
	public MailDto asHtml(boolean html) {
		this.html = html;
		return this;
	}
	
	public MailDto withAttachment(Attachment attachment) {
		attachments.add(attachment);
		return this;
	}
	
	public List<String> getRecipients() {
		return recipients;
	}

	public String getSubject() {
		return subject;
	}

	public String getBodyTemplate() {
		return bodyTemplate;
	}

	public boolean isHtml() {
		return html;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}
	
	public List<Inline> getInlines() {
		return inlines;
	}
	
	public Map<String,Object> getModel() {
		return model;
	}
	
	public MailDto withLogo(String logo) {
		this.logo = logo;
		return this;
	}
	
	public String getLogo() {
		return logo;
	}

	public class Attachment {
		private String attachmentFilename;
		private InputStreamSource inputStreamSource;
		private String contentType;
		
		public Attachment(String attachmentFilename, InputStreamSource inputStreamSource, String contentType) {
			super();
			this.attachmentFilename = attachmentFilename;
			this.inputStreamSource = inputStreamSource;
			this.contentType = contentType;
		}
		public String getAttachmentFilename() {
			return attachmentFilename;
		}
		public InputStreamSource getInputStreamSource() {
			return inputStreamSource;
		}
		public String getContentType() {
			return contentType;
		}
	}
	public class Inline {
		private String contendId;
		private InputStreamSource inputStreamSource;
		private String contentType;
		
		public Inline(String contendId, InputStreamSource inputStreamSource, String contentType) {
			super();
			this.contendId = contendId;
			this.inputStreamSource = inputStreamSource;
			this.contentType = contentType;
		}
		public String getContendId() {
			return contendId;
		}
		public InputStreamSource getInputStreamSource() {
			return inputStreamSource;
		}
		public String getContentType() {
			return contentType;
		}
	}
}
