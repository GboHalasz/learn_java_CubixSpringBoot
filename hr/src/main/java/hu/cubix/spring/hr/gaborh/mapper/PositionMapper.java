package hu.cubix.spring.hr.gaborh.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import hu.cubix.spring.hr.gaborh.dto.PositionDto;
import hu.cubix.spring.hr.gaborh.model.Position;

@Mapper(componentModel = "spring")
public interface PositionMapper {
	PositionDto positionToDto(Position position);

	Position dtoToPosition(PositionDto positionDto);

	List<PositionDto> positionsToDtos(List<Position> positions);

	List<Position> DtosToPositions(List<PositionDto> positions);
}
