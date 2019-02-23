package pl.itcraft.appstract.admin.inbox;

import pl.itcraft.appstract.admin.utils.AdminAppModuleConfig;
import pl.itcraft.appstract.core.entities.InboxMessageAttachment;

public class InboxMessageAttachmentDto {

	private String fileName;
	private String url;
	
	public InboxMessageAttachmentDto(InboxMessageAttachment attachment, AdminAppModuleConfig config) {
		this.fileName = attachment.getUploadedFile().getSourceFileName();
		this.url = config.getAbsoluteFileUrl(attachment.getUploadedFile().getPath());
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
