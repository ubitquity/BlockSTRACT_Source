package pl.itcraft.appstract.core.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "counties")
public class County extends BaseEntity {

	@Column(name = "name", unique = true)
	private String name;

	public County() {
	}

	public County(String name) {
		this.createdAt = new Date();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
