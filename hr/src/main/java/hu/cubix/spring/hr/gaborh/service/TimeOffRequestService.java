package hu.cubix.spring.hr.gaborh.service;

import static hu.cubix.spring.hr.gaborh.service.TimeOffRequestSpecifications.approverNameStartsWith;
import static hu.cubix.spring.hr.gaborh.service.TimeOffRequestSpecifications.createdAtBetween;
import static hu.cubix.spring.hr.gaborh.service.TimeOffRequestSpecifications.endDateAfterStartOrEqual;
import static hu.cubix.spring.hr.gaborh.service.TimeOffRequestSpecifications.startDateBeforeEndOrEqual;
import static hu.cubix.spring.hr.gaborh.service.TimeOffRequestSpecifications.status;
import static hu.cubix.spring.hr.gaborh.service.TimeOffRequestSpecifications.submitterNameStartsWith;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import hu.cubix.spring.hr.gaborh.dto.TimeOffRequestSearchDto;
import hu.cubix.spring.hr.gaborh.model.Employee;
import hu.cubix.spring.hr.gaborh.model.TimeOffRequest;
import hu.cubix.spring.hr.gaborh.model.TimeOffRequestStatus;
import hu.cubix.spring.hr.gaborh.repository.ManagerByCompanyRepository;
import hu.cubix.spring.hr.gaborh.repository.TimeOffRequestRepository;
import jakarta.transaction.Transactional;

@Service
public class TimeOffRequestService {

	@Autowired
	TimeOffRequestRepository timeOffRequestRepository;

	@Autowired
	ManagerByCompanyRepository managerByCompanyRepository;

	@Autowired
	EmployeeService employeeService;

	@Transactional
	public TimeOffRequest create(TimeOffRequest timeOffRequest) {

		if (findById(timeOffRequest.getId()).isPresent()) {
			return null;
		}

// Az útvonalat csak bejelentkezett felhasználó érheti el.
// A submitter a SecurityContext-ből a bejelentkezett felhasználó lesz

// Az approver beállító logikát kiemelni külön funkciókba és felhasználni az approve denie funkciókban!!		
		Optional<Employee> submitter = employeeService.findById(timeOffRequest.getSubmitter().getId());
		if (submitter.isEmpty()) {
			return null;
		}
//		timeOffRequest.setSubmitter(submitter.get());
		timeOffRequest.setApprover(null);
		timeOffRequest.setStatus(TimeOffRequestStatus.NOT_JUDGED);

//		List<ManagerByCompany> managersByCompany = managerByCompanyRepository
//				.findByCompany(timeOffRequest.getSubmitter().getCompany());
//		if (!managersByCompany.isEmpty()) {
//			timeOffRequest.setApprover(managersByCompany.get(0).getManager());
//		}

		return save(timeOffRequest);
	}

	@Transactional
	public TimeOffRequest update(TimeOffRequest timeOffRequest) {
		if (findById(timeOffRequest.getId()).isEmpty()) {
			return null;
		}
		return save(timeOffRequest);
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
	public TimeOffRequest approve(TimeOffRequest timeOffRequest) {
		timeOffRequest.setStatus(TimeOffRequestStatus.APPROVED);
//		timeOffRequest.setApprover(null);    						set from Security Context		
		return save(timeOffRequest);
	}
	
	@Transactional
	public TimeOffRequest reject(TimeOffRequest timeOffRequest) {
		timeOffRequest.setStatus(TimeOffRequestStatus.REJECTED);
//		timeOffRequest.setApprover(null);    						set from Security Context
		return save(timeOffRequest);
	}
}
