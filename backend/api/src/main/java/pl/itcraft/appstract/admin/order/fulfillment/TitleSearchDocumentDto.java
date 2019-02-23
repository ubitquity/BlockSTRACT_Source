package pl.itcraft.appstract.admin.order.fulfillment;

import pl.itcraft.appstract.admin.utils.AdminAppModuleConfig;
import pl.itcraft.appstract.core.entities.UploadedFile;

public class TitleSearchDocumentDto {

	private String fileName;
	private String url;
	
	public TitleSearchDocumentDto(UploadedFile file, AdminAppModuleConfig config) {
		this.fileName = file.getSourceFileName();
		this.url = config.getAbsoluteFileUrl(file.getPath());
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
