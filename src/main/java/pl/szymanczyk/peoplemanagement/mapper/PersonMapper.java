package pl.szymanczyk.peoplemanagement.mapper;

import org.mapstruct.Mapper;
import pl.szymanczyk.peoplemanagement.model.Person;
import pl.szymanczyk.peoplemanagement.model.PersonDto;

@Mapper
public interface PersonMapper {

    PersonDto mapToPersonDto(Person person);
}

