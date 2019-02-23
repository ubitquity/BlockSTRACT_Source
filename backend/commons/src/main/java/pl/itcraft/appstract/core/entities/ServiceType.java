package pl.itcraft.appstract.core.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "service_types")
public class ServiceType extends BaseEntity {

	@Column(name = "name")
	private String name;

	public String getName() {
		return name;
	}

}
