package hu.cubix.spring.hr.gaborh.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import hu.cubix.spring.hr.gaborh.model.TimeOffRequestStatus;

public class TimeOffRequestSearchDto {

	private long id;
	private LocalDateTime createdAt;
	private LocalDate createdAtIntervalStart;
	private LocalDate createdAtIntervalEnd;
	private LocalDate startDate;
	private LocalDate endDate;
	private String submitter;
	private String approver;
	private TimeOffRequestStatus status;

	public TimeOffRequestSearchDto() {

	}

	public TimeOffRequestSearchDto(long id, LocalDateTime createdAt, LocalDate createdAtIntervalStart,
			LocalDate createdAtIntervalEnd, LocalDate startDate, LocalDate endDate, String submitter, String approver,
			TimeOffRequestStatus status) {
		super();
		this.id = id;
		this.createdAt = createdAt;
		this.createdAtIntervalStart = createdAtIntervalStart;
		this.createdAtIntervalEnd = createdAtIntervalEnd;
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

	public LocalDate getCreatedAtIntervalStart() {
		return createdAtIntervalStart;
	}

	public void setCreatedAtIntervalStart(LocalDate createdAtIntervalStart) {
		this.createdAtIntervalStart = createdAtIntervalStart;
	}

	public LocalDate getCreatedAtIntervalEnd() {
		return createdAtIntervalEnd;
	}

	public void setCreatedAtIntervalEnd(LocalDate createdAtIntervalEnd) {
		this.createdAtIntervalEnd = createdAtIntervalEnd;
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

	public String getSubmitter() {
		return submitter;
	}

	public void setSubmitter(String submitter) {
		this.submitter = submitter;
	}

	public String getApprover() {
		return approver;
	}

	public void setApprover(String approver) {
		this.approver = approver;
	}

	public TimeOffRequestStatus getStatus() {
		return status;
	}

	public void setStatus(TimeOffRequestStatus status) {
		this.status = status;
	}
}
