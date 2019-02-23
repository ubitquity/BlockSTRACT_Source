package pl.itcraft.appstract.admin.reports;

public class MostResponsiveAbstractorDto {

	private Long id;
	private String name;
	private Long averageResponseTime;
	
	public MostResponsiveAbstractorDto() {}

	public MostResponsiveAbstractorDto(Long id, String name, Long averageResponseTime) {
		this.id = id;
		this.name = name;
		this.averageResponseTime = averageResponseTime;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getAverageResponseTime() {
		return averageResponseTime;
	}
	public void setAverageResponseTime(Long averageResponseTime) {
		this.averageResponseTime = averageResponseTime;
	}
	
}
