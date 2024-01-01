package hu.cubix.spring.hr.gaborh.model;

public enum Qualification {

	NONE("none"),
	HIGH_SCHOOL("high school"),
	COLLEGE("college"),
	UNIVERSITY("university"),
	PHD("PHD");
	
	private String value;

	private Qualification(String value) {
		this.setValue(value);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
