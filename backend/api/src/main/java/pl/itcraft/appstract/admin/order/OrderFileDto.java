package pl.itcraft.appstract.admin.order;

import pl.itcraft.appstract.admin.utils.AdminAppModuleConfig;
import pl.itcraft.appstract.core.entities.OrderFile;

public class OrderFileDto {

	private long id;
	private String fileName;
	private String url;
	
	public OrderFileDto(OrderFile file, AdminAppModuleConfig config) {
		this.id = file.getId();
		this.fileName = file.getUploadedFile().getSourceFileName();
		this.url = config.getAbsoluteFileUrl(file.getUploadedFile().getPath());
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
