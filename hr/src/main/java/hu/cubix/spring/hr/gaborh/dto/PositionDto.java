package hu.cubix.spring.hr.gaborh.dto;

import hu.cubix.spring.hr.gaborh.model.Qualification;

public class PositionDto {

	private long id;
	private String nameOfPosition;
	private Qualification qualification;

	public PositionDto() {

	}

	public PositionDto(String nameOfPosition, Qualification qualification) {
		super();
		this.nameOfPosition = nameOfPosition;
		this.qualification = qualification;
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

}
