package hu.cubix.spring.hr.gaborh.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import hu.cubix.spring.hr.gaborh.model.TimeOffRequestStatus;

public class TimeOffRequestDto {

	private long id;
	private LocalDateTime createdAt;
	private LocalDate startDate;
	private LocalDate endDate;
	private Long submitterId;
	private Long approverId;
	private TimeOffRequestStatus status;
	
	public TimeOffRequestDto() {

	}		
	
	public TimeOffRequestDto(LocalDateTime createdAt, LocalDate startDate, LocalDate endDate, Long submitterId, Long approverId,
			TimeOffRequestStatus status) {
		super();
		this.createdAt = createdAt;
		this.startDate = startDate;
		this.endDate = endDate;
		this.submitterId = submitterId;	
		this.approverId = approverId;
		this.status = status;		
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
	public long getSubmitterId() {
		return submitterId;
	}
	public void setSubmitterId(long submitterId) {
		this.submitterId = submitterId;
	}
	
	public Long getApproverId() {
		return approverId;
	}

	public void setApproverId(Long approverId) {
		this.approverId = approverId;
	}

	public TimeOffRequestStatus getStatus() {
		return status;
	}

	public void setStatus(TimeOffRequestStatus status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
}
