package hu.cubix.spring.hr.gaborh.service;

import org.springframework.stereotype.Service;

import hu.cubix.spring.hr.gaborh.model.Employee;

@Service
public class DefaultEmployeeService extends EmployeeSuperService {

	@Override
	public int getPayRaisePercent(Employee employee) {
		// TODO Auto-generated method stub
		return 5;
	}

}
