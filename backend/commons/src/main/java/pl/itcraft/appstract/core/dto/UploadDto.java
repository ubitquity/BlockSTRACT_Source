package pl.itcraft.appstract.core.dto;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class UploadDto {
	private String mimeType;
	private String base64Content;

	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public String getBase64Content() {
		return base64Content;
	}
	public void setBase64Content(String base64Content) {
		this.base64Content = base64Content;
	}
	@Override
	public String toString() {
		String content = StringUtils.hasLength(base64Content)
			? base64Content.substring(0, Math.min(base64Content.length(), 50)) + " (length: " + base64Content.length() + ")"
			: base64Content;
		return "UploadDto [mimeType=" + mimeType + ", base64Content=" + content + "]";
	}
	
}
