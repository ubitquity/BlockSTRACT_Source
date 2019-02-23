package pl.itcraft.appstract.admin.inbox;

import org.hibernate.validator.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;

public class InboxSendMessageDto {

	@ApiModelProperty(required = true)
	@NotEmpty
	private String content;
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
