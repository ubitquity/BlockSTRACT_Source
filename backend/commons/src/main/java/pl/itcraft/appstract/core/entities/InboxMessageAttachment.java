package pl.itcraft.appstract.core.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "inbox_message_attachments")
public class InboxMessageAttachment extends BaseEntity {

	@ManyToOne
	@JoinColumn(name = "inbox_message_id")
	private InboxMessage inboxMessage;
	
	@ManyToOne
	@JoinColumn(name = "uploaded_file_id")
	private UploadedFile uploadedFile;
	
	protected InboxMessageAttachment() {
		// JPA
	}
	
	public InboxMessageAttachment(InboxMessage inboxMessage, UploadedFile uploadedFile) {
		this.createdAt = new Date();
		this.inboxMessage = inboxMessage;
		this.uploadedFile = uploadedFile;
	}

	public InboxMessage getInboxMessage() {
		return inboxMessage;
	}

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}
	
}
