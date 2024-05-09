package pl.szymanczyk.peoplemanagement.mapper;

import javax.annotation.processing.Generated;
import pl.szymanczyk.peoplemanagement.model.Person;
import pl.szymanczyk.peoplemanagement.model.PersonDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-02-25T17:15:12+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 19.0.1 (Oracle Corporation)"
)
public class PersonMapperImpl implements PersonMapper {

    @Override
    public PersonDto mapToPersonDto(Person person) {
        if ( person == null ) {
            return null;
        }

        PersonDto.PersonDtoBuilder<?, ?> personDto = PersonDto.builder();

        personDto.id( (int) person.getId() );
        personDto.firstName( person.getFirstName() );
        personDto.lastName( person.getLastName() );
        personDto.mail( person.getMail() );
        personDto.personalId( person.getPersonalId() );
        personDto.height( person.getHeight() );
        personDto.weight( person.getWeight() );
        personDto.role( person.getRole() );
        personDto.password( person.getPassword() );
        personDto.blocked( person.isBlocked() );

        return personDto.build();
    }
}
