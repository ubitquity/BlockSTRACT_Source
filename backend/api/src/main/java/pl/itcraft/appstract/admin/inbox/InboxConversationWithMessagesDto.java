package pl.itcraft.appstract.admin.inbox;

import java.util.List;
import java.util.stream.Collectors;

import pl.itcraft.appstract.admin.utils.AdminAppModuleConfig;
import pl.itcraft.appstract.core.entities.InboxConversation;
import pl.itcraft.appstract.core.entities.InboxMessage;
import pl.itcraft.appstract.core.entities.Order;
import pl.itcraft.appstract.core.entities.User;
import pl.itcraft.appstract.core.enums.UserRole;

public class InboxConversationWithMessagesDto extends InboxConversationDto {

	private List<InboxMessageDto> messages;
	
	public InboxConversationWithMessagesDto(InboxConversation ic, User currentUser, List<InboxMessage> messages, AdminAppModuleConfig config) {
		super(ic, currentUser.getRole());
		if (currentUser.getRole() == UserRole.ABSTRACTOR && !currentUser.equals(ic.getLastMessage().getAssignedAbstractor())) {
			this.setLastMessageText(null);
			this.setLastMessageTime(null);
		}
		this.messages = messages.stream().map(m -> new InboxMessageDto(m, config)).collect(Collectors.toList());
	}
	
	public InboxConversationWithMessagesDto(Order order) {
		super(order);
	}

	public List<InboxMessageDto> getMessages() {
		return messages;
	}

	public void setMessages(List<InboxMessageDto> messages) {
		this.messages = messages;
	}
	
}
