package hu.cubix.spring.hr.gaborh.model;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Position {

	@Id
	@GeneratedValue
	private long id;
	private String nameOfPosition;
	private Qualification qualification;
	private long minSalary;

	public Position() {
		super();
	}

	public Position(String nameOfPosition, Qualification qualification, long minSalary) {
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

	@Override
	public int hashCode() {
		return Objects.hash(nameOfPosition);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		return Objects.equals(nameOfPosition, other.nameOfPosition);
	}

	

}
