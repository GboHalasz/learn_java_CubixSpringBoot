package hu.cubix.spring.hr.gaborh.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import hu.cubix.spring.hr.gaborh.dto.TimeOffRequestDto;
import hu.cubix.spring.hr.gaborh.model.TimeOffRequest;

@Mapper(componentModel = "spring")
public interface TimeOffRequestMapper {

	@Mapping(target = "submitterId", source = "submitter.id")
	@Mapping(target = "approverId", source = "approver.id")
	TimeOffRequestDto timeOffRequestToDto(TimeOffRequest timeOffRequest);

	@Mapping(target = "status", ignore = true)
	@Mapping(target = "approver", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@InheritInverseConfiguration
	TimeOffRequest dtoToTimeOffRequest(TimeOffRequestDto timeOffRequestDto);

	List<TimeOffRequestDto> timeOffRequestsToDtos(List<TimeOffRequest> timeOffRequests);

	List<TimeOffRequest> DtosToTimeOffRequests(List<TimeOffRequestDto> timeOffRequestDtos);
}
