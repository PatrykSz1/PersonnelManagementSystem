package pl.szymanczyk.peoplemanagement.mapper;

import org.mapstruct.Mapper;
import pl.szymanczyk.peoplemanagement.model.employeeposition.EmployeePosition;
import pl.szymanczyk.peoplemanagement.model.employeeposition.EmployeePositionDto;

@Mapper
public interface EmployeePositionMapper {
    EmployeePositionDto toDto(EmployeePosition employeePosition);

    EmployeePosition toEntity(EmployeePositionDto employeePositionDto);
}
