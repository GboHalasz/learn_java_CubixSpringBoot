package hu.cubix.spring.hr.gaborh.service;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import hu.cubix.spring.hr.gaborh.model.Company_;
import hu.cubix.spring.hr.gaborh.model.Employee;
import hu.cubix.spring.hr.gaborh.model.Employee_;
import hu.cubix.spring.hr.gaborh.model.Position_;
import jakarta.persistence.criteria.Expression;

public class EmployeeSpecifications {
	public static Specification<Employee> hasId(long id) {
		return (root, cq, cb) -> cb.equal(root.get(Employee_.id), id);
	}

	public static Specification<Employee> nameStartsWith(String prefix) {
		return (root, cq, cb) -> cb.like(cb.lower(root.get(Employee_.name)), prefix.toLowerCase() + "%");
	}

	public static Specification<Employee> hasNameOfPosition(String nameOfPosition) {
		return (root, cq, cb) -> cb.equal(root.get(Employee_.job).get(Position_.nameOfPosition), nameOfPosition);
	}

	public static Specification<Employee> salaryInFivePercent(int salary) {
		return (root, cq, cb) -> cb.between(root.get(Employee_.salary).as(Double.class), (salary * 0.95),
				(salary * 1.05));
	}

	public static Specification<Employee> hasSameEntryDay(LocalDate entryDay) {
		return (root, cq, cb) -> {
			Expression<LocalDate> startDateAsDate = cb.function("date", LocalDate.class, root.get(Employee_.startDate));
			return cb.equal(startDateAsDate, entryDay);
		};
	}

	public static Specification<Employee> companyStartsWith(String prefix) {
		return (root, cq, cb) -> cb.like(cb.lower(root.get(Employee_.company).get(Company_.name)),
				prefix.toLowerCase() + "%");
	}
}
