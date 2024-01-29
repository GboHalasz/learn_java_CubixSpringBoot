package hu.cubix.spring.hr.gaborh.service;

import static hu.cubix.spring.hr.gaborh.service.TimeOffRequestSpecifications.approverNameStartsWith;
import static hu.cubix.spring.hr.gaborh.service.TimeOffRequestSpecifications.createdAtBetween;
import static hu.cubix.spring.hr.gaborh.service.TimeOffRequestSpecifications.endDateAfterStartOrEqual;
import static hu.cubix.spring.hr.gaborh.service.TimeOffRequestSpecifications.startDateBeforeEndOrEqual;
import static hu.cubix.spring.hr.gaborh.service.TimeOffRequestSpecifications.status;
import static hu.cubix.spring.hr.gaborh.service.TimeOffRequestSpecifications.submitterNameStartsWith;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import hu.cubix.spring.hr.gaborh.dto.TimeOffRequestSearchDto;
import hu.cubix.spring.hr.gaborh.model.TimeOffRequest;
import hu.cubix.spring.hr.gaborh.model.TimeOffRequestStatus;
import hu.cubix.spring.hr.gaborh.repository.TimeOffRequestRepository;
import hu.cubix.spring.hr.gaborh.security.HrUser;
import jakarta.transaction.Transactional;

@Service
public class TimeOffRequestService {

	@Autowired
	TimeOffRequestRepository timeOffRequestRepository;

	@Autowired
	EmployeeService employeeService;

	@Transactional
	public TimeOffRequest create(TimeOffRequest timeOffRequest) {

		if (findById(timeOffRequest.getId()).isPresent()) {
			return null;
		}

		Long currentUserId = getCurrentHrUser().getId();

		timeOffRequest.setSubmitter(employeeService.findById(currentUserId).get());
		timeOffRequest.setApprover(null);
		timeOffRequest.setStatus(TimeOffRequestStatus.NOT_JUDGED);

		return save(timeOffRequest);
	}

	@Transactional
	public TimeOffRequest update(TimeOffRequest timeOffRequest) {
		TimeOffRequest storedRequest = findById(timeOffRequest.getId()).orElseThrow(() -> new NoSuchElementException());
		Long currentUserId = getCurrentHrUser().getId();
		
		if (!storedRequest.getSubmitter().getId().equals(currentUserId)) {
			throw new AccessDeniedException("You can only update your own requests!");
		}
		
		if (storedRequest.getStatus() != TimeOffRequestStatus.NOT_JUDGED)
			throw new AccessDeniedException("You can't update assessed requests!");
		
		storedRequest.setStartDate(timeOffRequest.getStartDate());
		storedRequest.setEndDate(timeOffRequest.getEndDate());
		
		return storedRequest;
	}

	@Transactional
	public TimeOffRequest save(TimeOffRequest timeOffRequest) {

		return timeOffRequestRepository.save(timeOffRequest);
	}

	public Page<TimeOffRequest> findAll(Pageable pageable) {
		return timeOffRequestRepository.findAll(pageable);
	}

	public Optional<TimeOffRequest> findById(long id) {
		return timeOffRequestRepository.findById(id);
	}

	@Transactional
	public void delete(long id) {
		timeOffRequestRepository.deleteById(id);
	}

	public List<TimeOffRequest> findTimeOffRequestsByExample(TimeOffRequestSearchDto timeOffRequestSearchDto) {

		TimeOffRequestStatus status = timeOffRequestSearchDto.getStatus();

		String submitterName = timeOffRequestSearchDto.getSubmitter();
		String approverName = timeOffRequestSearchDto.getApprover();

		LocalDate createdAtIntervalStart = timeOffRequestSearchDto.getCreatedAtIntervalStart();
		LocalDate createdAtIntervalEnd = timeOffRequestSearchDto.getCreatedAtIntervalEnd();

		LocalDate startDate = timeOffRequestSearchDto.getEndDate();
		LocalDate endDate = timeOffRequestSearchDto.getStartDate();

		Specification<TimeOffRequest> specs = Specification.where(null);

		if (status != null) {
			specs = specs.and(status(status));
		}

		if (StringUtils.hasLength(submitterName)) {
			specs = specs.and(submitterNameStartsWith(submitterName));
		}

		if (StringUtils.hasLength(approverName)) {
			specs = specs.and(approverNameStartsWith(approverName));
		}

		if (createdAtIntervalStart != null && createdAtIntervalEnd != null) {
			if (createdAtIntervalEnd.isBefore(createdAtIntervalStart)) {
				LocalDate temp = createdAtIntervalStart;
				createdAtIntervalStart = createdAtIntervalEnd;
				createdAtIntervalEnd = temp;
			}
			specs = specs.and(createdAtBetween(createdAtIntervalStart, createdAtIntervalEnd));
		}

		if (startDate != null && endDate != null) {
			if (endDate.isBefore(startDate)) {
				LocalDate temp = startDate;
				startDate = endDate;
				endDate = temp;
			}
			specs = specs.and(startDateBeforeEndOrEqual(startDate));
			specs = specs.and(endDateAfterStartOrEqual(endDate));
		}

		return timeOffRequestRepository.findAll(specs);
	}

	@Transactional
	public TimeOffRequest approve(long id) {
		TimeOffRequest request = findById(id).orElseThrow(() -> new NoSuchElementException());
		Long currentUserId = getCurrentHrUser().getId();

		if (!request.getSubmitter().getManager().getId().equals(currentUserId)) {
			throw new AccessDeniedException(
					"Trying to approve holiday request where the employee's manager is not the current user");
		}

		request.setStatus(TimeOffRequestStatus.APPROVED);
		request.setApprover(employeeService.findById(currentUserId).get());
		return request;
	}

	@Transactional
	public TimeOffRequest reject(long id) {
		TimeOffRequest request = findById(id).orElseThrow(() -> new NoSuchElementException());
		Long currentUserId = getCurrentHrUser().getId();
		if (!request.getSubmitter().getManager().getId().equals(currentUserId)) {
			throw new AccessDeniedException(
					"Trying to approve holiday request where the employee's manager is not the current user");
		}

		request.setStatus(TimeOffRequestStatus.REJECTED);
		request.setApprover(employeeService.findById(currentUserId).get());
		return request;
	}

	private HrUser getCurrentHrUser() {
		return (HrUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

}
