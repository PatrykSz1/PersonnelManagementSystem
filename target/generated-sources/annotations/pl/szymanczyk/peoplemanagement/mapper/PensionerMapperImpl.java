package pl.szymanczyk.peoplemanagement.mapper;

import java.util.Map;
import javax.annotation.processing.Generated;
import pl.szymanczyk.peoplemanagement.model.Role;
import pl.szymanczyk.peoplemanagement.model.pensioner.Pensioner;
import pl.szymanczyk.peoplemanagement.model.pensioner.PensionerDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-02-25T17:15:12+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 19.0.1 (Oracle Corporation)"
)
public class PensionerMapperImpl implements PensionerMapper {

    @Override
    public Pensioner mapToEntity(Map<String, String> args) {
        if ( args == null ) {
            return null;
        }

        Pensioner.PensionerBuilder<?, ?> pensioner = Pensioner.builder();

        if ( args.containsKey( "id" ) ) {
            pensioner.id( Long.parseLong( args.get( "id" ) ) );
        }
        if ( args.containsKey( "mail" ) ) {
            pensioner.mail( args.get( "mail" ) );
        }
        if ( args.containsKey( "personalId" ) ) {
            pensioner.personalId( args.get( "personalId" ) );
        }
        if ( args.containsKey( "firstName" ) ) {
            pensioner.firstName( args.get( "firstName" ) );
        }
        if ( args.containsKey( "lastName" ) ) {
            pensioner.lastName( args.get( "lastName" ) );
        }
        if ( args.containsKey( "height" ) ) {
            pensioner.height( Integer.parseInt( args.get( "height" ) ) );
        }
        if ( args.containsKey( "weight" ) ) {
            pensioner.weight( Integer.parseInt( args.get( "weight" ) ) );
        }
        if ( args.containsKey( "role" ) ) {
            pensioner.role( Enum.valueOf( Role.class, args.get( "role" ) ) );
        }
        if ( args.containsKey( "password" ) ) {
            pensioner.password( args.get( "password" ) );
        }
        if ( args.containsKey( "blocked" ) ) {
            pensioner.blocked( Boolean.parseBoolean( args.get( "blocked" ) ) );
        }
        if ( args.containsKey( "pension" ) ) {
            pensioner.pension( Integer.parseInt( args.get( "pension" ) ) );
        }
        if ( args.containsKey( "employmentYears" ) ) {
            pensioner.employmentYears( Integer.parseInt( args.get( "employmentYears" ) ) );
        }

        return pensioner.build();
    }

    @Override
    public PensionerDto mapToDto(Map<String, String> args) {
        if ( args == null ) {
            return null;
        }

        PensionerDto.PensionerDtoBuilder<?, ?> pensionerDto = PensionerDto.builder();

        if ( args.containsKey( "id" ) ) {
            pensionerDto.id( Integer.parseInt( args.get( "id" ) ) );
        }
        if ( args.containsKey( "firstName" ) ) {
            pensionerDto.firstName( args.get( "firstName" ) );
        }
        if ( args.containsKey( "lastName" ) ) {
            pensionerDto.lastName( args.get( "lastName" ) );
        }
        if ( args.containsKey( "mail" ) ) {
            pensionerDto.mail( args.get( "mail" ) );
        }
        if ( args.containsKey( "personalId" ) ) {
            pensionerDto.personalId( args.get( "personalId" ) );
        }
        if ( args.containsKey( "height" ) ) {
            pensionerDto.height( Integer.parseInt( args.get( "height" ) ) );
        }
        if ( args.containsKey( "weight" ) ) {
            pensionerDto.weight( Integer.parseInt( args.get( "weight" ) ) );
        }
        if ( args.containsKey( "role" ) ) {
            pensionerDto.role( Enum.valueOf( Role.class, args.get( "role" ) ) );
        }
        if ( args.containsKey( "password" ) ) {
            pensionerDto.password( args.get( "password" ) );
        }
        if ( args.containsKey( "blocked" ) ) {
            pensionerDto.blocked( Boolean.parseBoolean( args.get( "blocked" ) ) );
        }
        if ( args.containsKey( "pension" ) ) {
            pensionerDto.pension( Integer.parseInt( args.get( "pension" ) ) );
        }
        if ( args.containsKey( "employmentYears" ) ) {
            pensionerDto.employmentYears( Integer.parseInt( args.get( "employmentYears" ) ) );
        }

        return pensionerDto.build();
    }

    @Override
    public Pensioner fromDto(PensionerDto pensionerDto) {
        if ( pensionerDto == null ) {
            return null;
        }

        Pensioner.PensionerBuilder<?, ?> pensioner = Pensioner.builder();

        pensioner.id( pensionerDto.getId() );
        pensioner.mail( pensionerDto.getMail() );
        pensioner.personalId( pensionerDto.getPersonalId() );
        pensioner.firstName( pensionerDto.getFirstName() );
        pensioner.lastName( pensionerDto.getLastName() );
        pensioner.height( pensionerDto.getHeight() );
        pensioner.weight( pensionerDto.getWeight() );
        pensioner.role( pensionerDto.getRole() );
        pensioner.password( pensionerDto.getPassword() );
        pensioner.blocked( pensionerDto.isBlocked() );
        pensioner.pension( pensionerDto.getPension() );
        pensioner.employmentYears( pensionerDto.getEmploymentYears() );

        return pensioner.build();
    }
}
