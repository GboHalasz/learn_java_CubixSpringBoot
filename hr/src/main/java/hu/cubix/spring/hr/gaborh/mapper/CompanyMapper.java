package hu.cubix.spring.hr.gaborh.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import hu.cubix.spring.hr.gaborh.dto.CompanyDto;
import hu.cubix.spring.hr.gaborh.model.Company;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

	public CompanyDto companyToDto(Company company);

	public List<CompanyDto> companiesToDtos(List<Company> companies);

	public Company dtoToCompany(CompanyDto companyDto);

}
