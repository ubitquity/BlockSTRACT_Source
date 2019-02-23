package pl.itcraft.appstract.core.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "uploaded_files")
public class UploadedFile extends BaseEntity {

	@Column(name = "file_path")
	private String path;

	@Column(name = "mime_type")
	private String mimeType;

	@Column(name = "source_file_name")
	private String sourceFileName;

	@Column(name = "temporary_file")
	private boolean temporary;

	@Column(name = "secured")
	private boolean secured;
	
	@Column(name = "to_delete")
	private boolean toDelete;
	
	@Column(name = "entity_type")
	private String entityType;

	@Column(name = "entity_id")
	private Long entityId;
	
	@Transient
	private byte[] content;

	public UploadedFile() {
		// JPA
	}

	public UploadedFile(String sourceFileName, String path, String mimeType, boolean temporary, boolean secured, String entityType, Long entityId, byte[] content) {
		this.createdAt = new Date();
		this.path = path;
		this.mimeType = mimeType;
		this.temporary = temporary;
		this.secured = secured;
		this.sourceFileName = sourceFileName;
		this.entityType = entityType;
		this.entityId = entityId;
		this.content = content;
	}
	
	public String getPath() {
		return path;
	}

	public boolean isTemporary() {
		return temporary;
	}
	
	public boolean isSecured() {
		return secured;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void markAsToDelete() {
		this.toDelete = true;
	}
	
	public boolean isToDelete() {
		return toDelete;
	}

	public String getSourceFileName() {
		return sourceFileName;
	}

	public String getEntityType() {
		return entityType;
	}

	public Long getEntityId() {
		return entityId;
	}

	public byte[] getContent() {
		return content;
	}

}
