package hu.cubix.spring.hr.gaborh.dto;

import hu.cubix.spring.hr.gaborh.model.Qualification;

public class PositionDto {

	private long id;
	private String nameOfPosition;
	private Qualification qualification;
	private long minSalary;

	public PositionDto() {

	}

	public PositionDto(String nameOfPosition, Qualification qualification, long minSalary) {
		super();
		this.nameOfPosition = nameOfPosition;
		this.qualification = qualification;
		this.minSalary = minSalary;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNameOfPosition() {
		return nameOfPosition;
	}

	public void setNameOfPosition(String nameOfPosition) {
		this.nameOfPosition = nameOfPosition;
	}

	public Qualification getQualification() {
		return qualification;
	}

	public void setQualification(Qualification qualification) {
		this.qualification = qualification;
	}

	public long getMinSalary() {
		return minSalary;
	}

	public void setMinSalary(long minSalary) {
		this.minSalary = minSalary;
	}

}
