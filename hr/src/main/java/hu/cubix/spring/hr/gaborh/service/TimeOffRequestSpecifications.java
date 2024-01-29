package hu.cubix.spring.hr.gaborh.service;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import hu.cubix.spring.hr.gaborh.model.Employee_;
import hu.cubix.spring.hr.gaborh.model.TimeOffRequest;
import hu.cubix.spring.hr.gaborh.model.TimeOffRequestStatus;
import hu.cubix.spring.hr.gaborh.model.TimeOffRequest_;

public class TimeOffRequestSpecifications {

	public static Specification<TimeOffRequest> status(TimeOffRequestStatus status) {
		return (root, cq, cb) -> cb.equal(root.get(TimeOffRequest_.status), status);
	}

	public static Specification<TimeOffRequest> submitterNameStartsWith(String prefix) {
		return (root, cq, cb) -> cb.like(cb.lower(root.get(TimeOffRequest_.submitter).get(Employee_.name)),
				prefix.toLowerCase() + "%");
	}

	public static Specification<TimeOffRequest> approverNameStartsWith(String prefix) {
		return (root, cq, cb) -> cb.like(cb.lower(root.get(TimeOffRequest_.approver).get(Employee_.name)),
				prefix.toLowerCase() + "%");
	}

	public static Specification<TimeOffRequest> createdAtBetween(LocalDate start, LocalDate end) {
		return (root, cq, cb) -> {
			return cb.between(root.get(TimeOffRequest_.createdAt).as(LocalDate.class), start, end);
		};
	}

	public static Specification<TimeOffRequest> startDateBeforeEndOrEqual(LocalDate startDate) {
		return (root, cq, cb) -> cb.greaterThanOrEqualTo(root.get(TimeOffRequest_.endDate), startDate);
	}

	public static Specification<TimeOffRequest> endDateAfterStartOrEqual(LocalDate endDate) {
		return (root, cq, cb) -> cb.lessThanOrEqualTo(root.get(TimeOffRequest_.startDate), endDate);
	}

}
