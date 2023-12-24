package hu.cubix.spring.hr.gaborh.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import hu.cubix.spring.hr.gaborh.dto.EmployeeDto;
import hu.cubix.spring.hr.gaborh.model.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

	EmployeeDto employeeToDto(Employee employee);

	Employee dtoToEmployee(EmployeeDto employeeDto);
	
	List<EmployeeDto> employeesToDtos(List<Employee> employees);
	
	List<Employee> DtosToEmployees(List<EmployeeDto> employees);


}
