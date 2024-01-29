package hu.cubix.spring.hr.gaborh.mapper;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import hu.cubix.spring.hr.gaborh.dto.EmployeeDto;
import hu.cubix.spring.hr.gaborh.model.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

	@Mapping(target = "company.employees", ignore = true)
	EmployeeDto employeeToDto(Employee employee);

	@Mapping(target = "company", ignore = true)
	@Named("summary")
	Employee dtoToEmployee(EmployeeDto employeeDto);

	Employee dtoToEmployeeWithCompany(EmployeeDto employeeDto);

	List<EmployeeDto> employeesToDtos(List<Employee> employees);

	@IterableMapping(qualifiedByName = "summary")
	List<Employee> DtosToEmployees(List<EmployeeDto> employees);

}
