package pl.szymanczyk.peoplemanagement.mapper;

import javax.annotation.processing.Generated;
import pl.szymanczyk.peoplemanagement.model.employeeposition.EmployeePosition;
import pl.szymanczyk.peoplemanagement.model.employeeposition.EmployeePositionDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-02-25T17:15:12+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 19.0.1 (Oracle Corporation)"
)
public class EmployeePositionMapperImpl implements EmployeePositionMapper {

    @Override
    public EmployeePositionDto toDto(EmployeePosition employeePosition) {
        if ( employeePosition == null ) {
            return null;
        }

        EmployeePositionDto.EmployeePositionDtoBuilder employeePositionDto = EmployeePositionDto.builder();

        if ( employeePosition.getId() != null ) {
            employeePositionDto.id( employeePosition.getId().intValue() );
        }
        employeePositionDto.startDate( employeePosition.getStartDate() );
        employeePositionDto.endDate( employeePosition.getEndDate() );
        employeePositionDto.position( employeePosition.getPosition() );
        employeePositionDto.salary( employeePosition.getSalary() );

        return employeePositionDto.build();
    }

    @Override
    public EmployeePosition toEntity(EmployeePositionDto employeePositionDto) {
        if ( employeePositionDto == null ) {
            return null;
        }

        EmployeePosition.EmployeePositionBuilder employeePosition = EmployeePosition.builder();

        employeePosition.id( (long) employeePositionDto.getId() );
        employeePosition.startDate( employeePositionDto.getStartDate() );
        employeePosition.endDate( employeePositionDto.getEndDate() );
        employeePosition.position( employeePositionDto.getPosition() );
        employeePosition.salary( employeePositionDto.getSalary() );

        return employeePosition.build();
    }
}
