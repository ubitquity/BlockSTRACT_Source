package pl.itcraft.appstract.core.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@MappedSuperclass
public class BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;
	
	@Version
	@Column(name = "version", nullable = false)
	protected long version;
	
	@Column(name = "created_at", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	protected Date createdAt;
	
	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	public Long getIdOrZero() {
		return isNew() ? 0 : id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public boolean isNew() {
		return id == null;
	}
	
	public long getVersion() {
		return version;
	}

	@Override
	public int hashCode() {
		int hash = 53;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}
	
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null) {
			return false;
		}
		if (!this.getClass().isInstance(other)) {
			return false;
		}
		BaseEntity obj2 = (BaseEntity) other;
		if (this == obj2) {
			return true;
		}
		if (this.isNew() || obj2.isNew()) {
			return false;
		}
		return this.getId().equals(obj2.getId());
	}

	@Override
	public String toString() {
		return getClass().getSimpleName()+"[id=" + id + "]";
	}

}
