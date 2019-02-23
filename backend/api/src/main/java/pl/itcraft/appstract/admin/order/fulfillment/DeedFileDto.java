package pl.itcraft.appstract.admin.order.fulfillment;

import pl.itcraft.appstract.admin.utils.AdminAppModuleConfig;
import pl.itcraft.appstract.core.entities.OrderDeedFile;
import pl.itcraft.appstract.core.entities.UploadedFile;

public class DeedFileDto {

	private long deedFileId;
	private String fileName;
	private String url;
	
	public DeedFileDto(OrderDeedFile deedFile, AdminAppModuleConfig config) {
		this.deedFileId = deedFile.getId();
		UploadedFile uploadedFile = deedFile.getUploadedFile();
		this.fileName = uploadedFile.getSourceFileName();
		this.url = config.getAbsoluteFileUrl(uploadedFile.getPath());
	}
	
	public long getDeedFileId() {
		return deedFileId;
	}
	public void setDeedFileId(long deedFileId) {
		this.deedFileId = deedFileId;
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
