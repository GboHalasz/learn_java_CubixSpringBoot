package hu.cubix.spring.hr.gaborh.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import hu.cubix.spring.hr.gaborh.dto.TimeOffRequestDto;
import hu.cubix.spring.hr.gaborh.dto.TimeOffRequestSearchDto;
import hu.cubix.spring.hr.gaborh.mapper.TimeOffRequestMapper;
import hu.cubix.spring.hr.gaborh.model.TimeOffRequest;
import hu.cubix.spring.hr.gaborh.model.TimeOffRequestStatus;
import hu.cubix.spring.hr.gaborh.security.HrUser;
import hu.cubix.spring.hr.gaborh.service.TimeOffRequestService;

@RestController
@RequestMapping("/api/timeoffs")
public class HrTimeOffRequestsRestController {

	@Autowired
	TimeOffRequestService timeOffRequestService;

	@Autowired
	TimeOffRequestMapper timeOffRequestMapper;

	@GetMapping
	public List<TimeOffRequestDto> findAll(@SortDefault("id") Pageable pageable /* query param: page, size, sort */) {

		List<TimeOffRequest> timeOffRequests = null;

		Page<TimeOffRequest> timeOffRequestPage = timeOffRequestService.findAll(pageable);
		timeOffRequests = timeOffRequestPage.getContent();

		return timeOffRequestMapper.timeOffRequestsToDtos(timeOffRequests);
	}

	@GetMapping("/{id}")
	public TimeOffRequestDto findById(@PathVariable long id) {
		TimeOffRequest timeOffRequest = findByIdOrThrow(id);
		return timeOffRequestMapper.timeOffRequestToDto(timeOffRequest);
	}

	private TimeOffRequest findByIdOrThrow(long id) {
		return timeOffRequestService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@PostMapping("/search")
	public List<TimeOffRequestDto> findTimeOffRequestsByExample(
			@RequestBody TimeOffRequestSearchDto timeOffRequestSearchDto) {
		List<TimeOffRequest> timeOffRequests = timeOffRequestService
				.findTimeOffRequestsByExample(timeOffRequestSearchDto);

		return timeOffRequestMapper.timeOffRequestsToDtos(timeOffRequests);
	}

	@PostMapping
	//@PreAuthorize("#timeOffRequestDto.submitterId == authentication.principal.employee.id")
	public TimeOffRequestDto create(@RequestBody TimeOffRequestDto timeOffRequestDto) {
		
		TimeOffRequest timeOffRequest = timeOffRequestMapper.dtoToTimeOffRequest(timeOffRequestDto);
		TimeOffRequest savedTimeOffRequest = timeOffRequestService.create(timeOffRequest);

		if (savedTimeOffRequest == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return timeOffRequestMapper.timeOffRequestToDto(savedTimeOffRequest);
	}

	@PutMapping("/{id}")
	public TimeOffRequestDto update(@PathVariable long id, @RequestBody TimeOffRequestDto timeOffRequestDto) {
		timeOffRequestDto.setId(id);
		TimeOffRequest updatedTimeOffRequest;
		try {
			updatedTimeOffRequest = timeOffRequestService
					.update(timeOffRequestMapper.dtoToTimeOffRequest(timeOffRequestDto));

		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		} catch (AccessDeniedException e) {
			throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED);
		}

		return timeOffRequestMapper.timeOffRequestToDto(updatedTimeOffRequest);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable long id) {
		TimeOffRequest timeOffRequestEntity = findByIdOrThrow(id);
		
		if (!timeOffRequestEntity.getSubmitter().getId().equals(getCurrentHrUser().getEmployee().getId())) {
			throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED);
		}
		
		if (timeOffRequestEntity.getStatus() != TimeOffRequestStatus.NOT_JUDGED)
			throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED);

		timeOffRequestService.delete(id);
	}

	private HrUser getCurrentHrUser() {
		return (HrUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	@GetMapping("/{id}/approve")
	public TimeOffRequestDto approve(@PathVariable long id) {
		TimeOffRequest approvedRequest;
		try {
			approvedRequest = timeOffRequestService.approve(id);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		} catch (AccessDeniedException e) {
			throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED);
		}
		return timeOffRequestMapper.timeOffRequestToDto(approvedRequest);
	}

	@GetMapping("/{id}/reject")
	public TimeOffRequestDto reject(@PathVariable long id) {
		TimeOffRequest rejectedRequest;
		try {
			rejectedRequest = timeOffRequestService.reject(id);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		} catch (AccessDeniedException e) {
			throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED);
		}
		return timeOffRequestMapper.timeOffRequestToDto(rejectedRequest);
	}
}
