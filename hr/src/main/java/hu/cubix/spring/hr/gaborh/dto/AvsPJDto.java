package hu.cubix.spring.hr.gaborh.dto;

public class AvsPJDto {

	private String nameOfPosition;
	private double avs;

	public AvsPJDto() {

	}

	public AvsPJDto(String nameOfPosition, double avs) {
		this.nameOfPosition = nameOfPosition;
		this.avs = avs;
	}

	public String getNameOfPosition() {
		return nameOfPosition;
	}

	public void setNameOfPosition(String nameOfPosition) {
		this.nameOfPosition = nameOfPosition;
	}

	public double getAvs() {
		return avs;
	}

	public void setAvs(double avs) {
		this.avs = avs;
	}

}
