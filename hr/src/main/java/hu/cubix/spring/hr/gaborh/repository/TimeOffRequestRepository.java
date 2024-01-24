package hu.cubix.spring.hr.gaborh.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import hu.cubix.spring.hr.gaborh.model.TimeOffRequest;

public interface TimeOffRequestRepository
		extends JpaRepository<TimeOffRequest, Long>, JpaSpecificationExecutor<TimeOffRequest> {

}
