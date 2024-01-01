package hu.cubix.spring.hr.gaborh.mapper;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import hu.cubix.spring.hr.gaborh.dto.CompanyDto;
import hu.cubix.spring.hr.gaborh.dto.EmployeeDto;
import hu.cubix.spring.hr.gaborh.model.Company;
import hu.cubix.spring.hr.gaborh.model.Employee;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

	CompanyDto companyToDto(Company company);

	List<CompanyDto> companiesToDtos(List<Company> companies);

	Company dtoToCompany(CompanyDto companyDto);
	
	@Mapping(target = "employees", ignore = true)
	@Named("summary")
	CompanyDto companyToSummaryDto(Company company);
	
	@IterableMapping(qualifiedByName = "summary")
	List<CompanyDto> companiesToSummaryDtos(List<Company> companies);
	
// employee mapperek a company mapperen belül, hogy külön kezeljük, amikor a company oldalról, a company controllerben mappeljük az employee-kat

	EmployeeDto employeeToDto(Employee employee);

	@Mapping(target = "company", ignore = true)
	Employee dtoToEmployee(EmployeeDto employeeDto);

	List<Employee> dtosToEmployees(List<EmployeeDto> employeeDtos);
}
