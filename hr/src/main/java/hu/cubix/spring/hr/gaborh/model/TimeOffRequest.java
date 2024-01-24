package hu.cubix.spring.hr.gaborh.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class TimeOffRequest {

	@Id
	@GeneratedValue
	private long id;
	private LocalDateTime createdAt = LocalDateTime.now();
	private LocalDate startDate;
	private LocalDate endDate;

	@ManyToOne
	@JoinColumn(name = "employee_id")
	private Employee submitter;
	
	@ManyToOne
	@JoinColumn(name = "approver_id")
	private Employee approver;
	
	private TimeOffRequestStatus status = TimeOffRequestStatus.NOT_JUDGED;	

	public TimeOffRequest() {

	}

	public TimeOffRequest(LocalDateTime createdAt, LocalDate startDate, LocalDate endDate, Employee submitter, Employee approver, TimeOffRequestStatus status) {
		this.createdAt = createdAt;
		this.startDate = startDate;
		this.endDate = endDate;
		this.submitter = submitter;
		this.approver = approver;
		this.status = status;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}	

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public Employee getSubmitter() {
		return submitter;
	}

	public void setSubmitter(Employee submitter) {
		this.submitter = submitter;
	}

	

	public Employee getApprover() {
		return approver;
	}

	public void setApprover(Employee approver) {
		this.approver = approver;
	}

	public TimeOffRequestStatus getStatus() {
		return status;
	}

	public void setStatus(TimeOffRequestStatus status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimeOffRequest other = (TimeOffRequest) obj;
		return id == other.id;
	}

}
