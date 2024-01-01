package hu.cubix.spring.hr.gaborh.dto;

public class CompanyFormDto {

	private long id;
	private String name;
	
	public CompanyFormDto() {
		
	}

	public CompanyFormDto(long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
