package hu.cubix.spring.hr.gaborh.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import hu.cubix.spring.hr.gaborh.dto.CompanyFormDto;
import hu.cubix.spring.hr.gaborh.model.CompanyForm;

@Mapper(componentModel = "spring")
public interface CompanyFormMapper {

	CompanyFormDto companyFormToDto(CompanyForm companyForm);

	CompanyForm dtoToCompanyForm(CompanyFormDto companyFormDto);
	
	List<CompanyFormDto> companyFormsToDtos(List<CompanyForm> companyForms);
	
	List<CompanyForm> DtosToCompanyForms(List<CompanyFormDto> companyForms);


}
