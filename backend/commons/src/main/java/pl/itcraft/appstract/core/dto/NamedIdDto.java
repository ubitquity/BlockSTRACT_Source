package pl.itcraft.appstract.core.dto;

public class NamedIdDto {
	private Long id;
	private String name;

	public NamedIdDto(long id, String name) {
		this.id = id;
		this.name = name;
	}
	public Long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	
}
