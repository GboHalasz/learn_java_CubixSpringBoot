package hu.cubix.spring.hr.gaborh.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import hu.cubix.spring.hr.gaborh.dto.EmployeeDto;
import hu.cubix.spring.hr.gaborh.model.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

	public EmployeeDto employeeToDto(Employee Employee);

	public List<EmployeeDto> employeesToDtos(List<Employee> Employees);

	public Employee dtoToEmployee(EmployeeDto EmployeeDto);

}
