package pl.szymanczyk.peoplemanagement.mapper;

import jakarta.validation.Valid;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import pl.szymanczyk.peoplemanagement.model.PersonRequestDto;
import pl.szymanczyk.peoplemanagement.model.employee.Employee;
import pl.szymanczyk.peoplemanagement.model.employee.EmployeeDto;
import pl.szymanczyk.peoplemanagement.model.pensioner.Pensioner;
import pl.szymanczyk.peoplemanagement.model.pensioner.PensionerDto;

import java.util.Map;

@Mapper
public interface PensionerMapper {


    Pensioner mapToEntity(Map<String, String> args);

    PensionerDto mapToDto(Map<String,String> args);

    Pensioner fromDto(@Valid PensionerDto pensionerDto);
}