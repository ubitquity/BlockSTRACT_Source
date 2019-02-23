package pl.itcraft.appstract.admin.inbox;

import java.util.List;
import java.util.stream.Collectors;

import pl.itcraft.appstract.admin.utils.AdminAppModuleConfig;
import pl.itcraft.appstract.core.entities.InboxMessage;
import pl.itcraft.appstract.core.entities.User;

public class InboxMessageDto {
	
	private String sender;
	private String content;
	private Long createdAt;
	private List<InboxMessageAttachmentDto> attachments;

	public InboxMessageDto(InboxMessage message, AdminAppModuleConfig config) {
		User sender = message.getSender();
		this.sender = sender.getFirstName() + " " + sender.getLastName();
		this.content = message.getContent();
		this.createdAt = message.getCreatedAt().getTime();
		this.attachments = message.getInboxMessageAttachments().stream().map(ma -> new InboxMessageAttachmentDto(ma, config)).collect(Collectors.toList());
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Long createdAt) {
		this.createdAt = createdAt;
	}

	public List<InboxMessageAttachmentDto> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<InboxMessageAttachmentDto> attachments) {
		this.attachments = attachments;
	}

}
