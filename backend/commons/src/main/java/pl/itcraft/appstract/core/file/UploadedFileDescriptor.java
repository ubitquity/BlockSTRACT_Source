package pl.itcraft.appstract.core.file;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.springframework.web.multipart.MultipartFile;

public class UploadedFileDescriptor {
	
	// TODO: apache/conf/mime.types
	public static Map<String, String> MIME_TO_EXT = new HashMap<>();
	static {
		MIME_TO_EXT.put("image/jpeg", "jpg");
		MIME_TO_EXT.put("image/png", "png");
		MIME_TO_EXT.put("application/pdf", "pdf");
		MIME_TO_EXT.put("text/plain", "txt");
		MIME_TO_EXT.put("text/html", "html");
		MIME_TO_EXT.put("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "docx");
		MIME_TO_EXT.put("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "xlsx");
		MIME_TO_EXT.put("application/zip", "zip");
	}
	
	private String sourceName = "unknown";
	private String base64;
	private String mimeType;
	private boolean temporary = false;
	private boolean secured = true;
	private MultipartFile multipartFile;
	private String entityType = null;
	private Long entityId = null;
	
	private UploadedFileDescriptor() {
	}
	
	public static UploadedFileDescriptor create() {
		UploadedFileDescriptor ufd = new UploadedFileDescriptor();
		return ufd;
	}
	
	/**
	 * Obsluguje czysty base64 lub w formacie 'data:image/jpeg;base64,BASE64BODY....'
	 */
	public UploadedFileDescriptor withBase64(String base64) {
		final String PREFIX = "data:";
		int start = base64.indexOf(PREFIX);
		if (start < 0) {
			this.base64 = base64;
			return this;
		}
		start += PREFIX.length();
		int end = base64.indexOf(";", start);
		if (end < 0) {
			this.base64 = base64;
			return this;
		}
		this.mimeType = base64.substring(start, end);
		this.base64 = base64.split(",", 2)[1];
		return this;
	}
	
	public UploadedFileDescriptor withMultipartFile(MultipartFile multipartFile) {
		this.multipartFile = multipartFile;
		this.mimeType = multipartFile.getContentType();
		this.sourceName = multipartFile.getOriginalFilename();
		return this;
	}
	
	public UploadedFileDescriptor withMimeType(String mimeType) {
		this.mimeType = mimeType;
		return this;
	}
	
	public UploadedFileDescriptor withSourceName(String sourceName) {
		this.sourceName = sourceName;
		return this;
	}
	
	public UploadedFileDescriptor withBaseSourceName(String baseSourceName) {
		if (this.mimeType == null) {
			throw new RuntimeException("No MIME type defined");
		}
		String ext = MIME_TO_EXT.get(this.mimeType);
		if (ext != null) {
			this.sourceName = baseSourceName + "." + ext;
		} else {
			this.sourceName = baseSourceName + ".bin";
		}
		return this;
	}
	
	public UploadedFileDescriptor withTemporary(boolean temporary) {
		this.temporary = temporary;
		return this;
	}
	
	public UploadedFileDescriptor withSecured(boolean secured) {
		this.secured = secured;
		return this;
	}
	
	public UploadedFileDescriptor withEntityRef(String entityType, Long entityId) {
		this.entityType = entityType;
		this.entityId = entityId;
		return this;
	}
	
	public String getSourceName() {
		return sourceName;
	}

	public String getMimeType() {
		return mimeType;
	}
	
	public String getEntityType() {
		return entityType;
	}
	
	public Long getEntityId() {
		return entityId;
	}
	
	public byte[] getBytes() {
		try {
			if (base64 != null) {
				return Base64.decodeBase64(base64);
			} else if (multipartFile != null) {
				return multipartFile.getBytes();
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new RuntimeException("Reading file content failed for ["+sourceName+"]", e);
		}
	}
	
	public boolean isTemporary() {
		return temporary;
	}
	
	public boolean isSecured() {
		return secured;
	}
	
}
