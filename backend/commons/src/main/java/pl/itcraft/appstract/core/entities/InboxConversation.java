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

import pl.itcraft.appstract.core.api.error.ApiException;
import pl.itcraft.appstract.core.api.error.RC;
import pl.itcraft.appstract.core.enums.UserRole;

@Entity
@Table(name = "inbox_conversations")
public class InboxConversation extends BaseEntity {

	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;
	
	@OneToMany(mappedBy = "inboxConversation")
	@OrderBy("id ASC")
	@Cascade(value = CascadeType.ALL)
	private List<InboxMessage> inboxMessages;
	
	@ManyToOne
	@JoinColumn(name = "last_message_id")
	private InboxMessage lastMessage;
	
	@Column(name = "admin_new_message")
	private boolean adminNewMessage;
	
	@Column(name = "abstractor_new_message")
	private boolean abstractorNewMessage;
	
	protected InboxConversation() {
		// JPA
	}
	
	public InboxConversation(Order order) {
		this.createdAt = new Date();
		this.order = order;
	}

	public Order getOrder() {
		return order;
	}

	public List<InboxMessage> getInboxMessages() {
		return inboxMessages;
	}

	public InboxMessage getLastMessage() {
		return lastMessage;
	}

	public void setLastMessage(InboxMessage lastMessage) {
		if (!this.equals(lastMessage.getInboxConversation())) {
			throw new ApiException(RC.BUSINESS_ERROR);
		}
		this.lastMessage = lastMessage;
		if (lastMessage.getSender().getRole() == UserRole.ADMIN) {
			this.abstractorNewMessage = true;
		} else {
			this.adminNewMessage = true;
		}
	}

	public boolean isAdminNewMessage() {
		return adminNewMessage;
	}

	public boolean isAbstractorNewMessage() {
		return abstractorNewMessage;
	}
	
	public void setMessagesAsRead(UserRole role) {
		if (role == UserRole.ADMIN) {
			this.adminNewMessage = false;
		} else {
			this.abstractorNewMessage = false;
		}
	}
	
}
