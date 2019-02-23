package pl.itcraft.appstract.core.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "inbox_messages")
public class InboxMessage extends BaseEntity {

	@ManyToOne
	@JoinColumn(name = "inbox_conversation_id")
	private InboxConversation inboxConversation;
	
	@ManyToOne
	@JoinColumn(name = "sender_id")
	private User sender;
	
	@ManyToOne
	@JoinColumn(name = "assigned_abstractor_id")
	private User assignedAbstractor;
	
	@Column(name = "content")
	private String content;
	
	@OneToMany(mappedBy = "inboxMessage")
	@OrderBy("id ASC")
	@Cascade(value = CascadeType.ALL)
	private List<InboxMessageAttachment> inboxMessageAttachments;
	
	protected InboxMessage() {
		// JPA
	}
	
	public InboxMessage(InboxConversation inboxConversation, User sender, User assignedAbstractor, String content, List<InboxMessageAttachment> inboxMessageAttachments) {
		this.createdAt = new Date();
		this.inboxConversation = inboxConversation;
		this.sender = sender;
		this.assignedAbstractor = assignedAbstractor;
		this.content = content;
		this.inboxMessageAttachments = inboxMessageAttachments;
	}

	public InboxConversation getInboxConversation() {
		return inboxConversation;
	}

	public String getContent() {
		return content;
	}

	public List<InboxMessageAttachment> getInboxMessageAttachments() {
		return inboxMessageAttachments;
	}

	public User getSender() {
		return sender;
	}
	
	public User getAssignedAbstractor() {
		return assignedAbstractor;
	}

	public void setAsLastMessageInConversation() {
		this.inboxConversation.setLastMessage(this);
	}
	
}
