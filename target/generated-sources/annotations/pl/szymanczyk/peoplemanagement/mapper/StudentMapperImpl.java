package pl.szymanczyk.peoplemanagement.mapper;

import java.math.BigDecimal;
import java.util.Map;
import javax.annotation.processing.Generated;
import pl.szymanczyk.peoplemanagement.model.Role;
import pl.szymanczyk.peoplemanagement.model.student.Student;
import pl.szymanczyk.peoplemanagement.model.student.StudentDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-02-25T17:15:13+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 19.0.1 (Oracle Corporation)"
)
public class StudentMapperImpl implements StudentMapper {

    @Override
    public Student mapToEntity(Map<String, String> args) {
        if ( args == null ) {
            return null;
        }

        Student.StudentBuilder<?, ?> student = Student.builder();

        if ( args.containsKey( "id" ) ) {
            student.id( Long.parseLong( args.get( "id" ) ) );
        }
        if ( args.containsKey( "mail" ) ) {
            student.mail( args.get( "mail" ) );
        }
        if ( args.containsKey( "personalId" ) ) {
            student.personalId( args.get( "personalId" ) );
        }
        if ( args.containsKey( "firstName" ) ) {
            student.firstName( args.get( "firstName" ) );
        }
        if ( args.containsKey( "lastName" ) ) {
            student.lastName( args.get( "lastName" ) );
        }
        if ( args.containsKey( "height" ) ) {
            student.height( Integer.parseInt( args.get( "height" ) ) );
        }
        if ( args.containsKey( "weight" ) ) {
            student.weight( Integer.parseInt( args.get( "weight" ) ) );
        }
        if ( args.containsKey( "role" ) ) {
            student.role( Enum.valueOf( Role.class, args.get( "role" ) ) );
        }
        if ( args.containsKey( "password" ) ) {
            student.password( args.get( "password" ) );
        }
        if ( args.containsKey( "blocked" ) ) {
            student.blocked( Boolean.parseBoolean( args.get( "blocked" ) ) );
        }
        if ( args.containsKey( "academyName" ) ) {
            student.academyName( args.get( "academyName" ) );
        }
        if ( args.containsKey( "studyYear" ) ) {
            student.studyYear( Integer.parseInt( args.get( "studyYear" ) ) );
        }
        if ( args.containsKey( "studyField" ) ) {
            student.studyField( args.get( "studyField" ) );
        }
        if ( args.containsKey( "scholarship" ) ) {
            student.scholarship( new BigDecimal( args.get( "scholarship" ) ) );
        }

        return student.build();
    }

    @Override
    public StudentDto mapToDto(Map<String, String> args) {
        if ( args == null ) {
            return null;
        }

        StudentDto.StudentDtoBuilder<?, ?> studentDto = StudentDto.builder();

        if ( args.containsKey( "id" ) ) {
            studentDto.id( Integer.parseInt( args.get( "id" ) ) );
        }
        if ( args.containsKey( "firstName" ) ) {
            studentDto.firstName( args.get( "firstName" ) );
        }
        if ( args.containsKey( "lastName" ) ) {
            studentDto.lastName( args.get( "lastName" ) );
        }
        if ( args.containsKey( "mail" ) ) {
            studentDto.mail( args.get( "mail" ) );
        }
        if ( args.containsKey( "personalId" ) ) {
            studentDto.personalId( args.get( "personalId" ) );
        }
        if ( args.containsKey( "height" ) ) {
            studentDto.height( Integer.parseInt( args.get( "height" ) ) );
        }
        if ( args.containsKey( "weight" ) ) {
            studentDto.weight( Integer.parseInt( args.get( "weight" ) ) );
        }
        if ( args.containsKey( "role" ) ) {
            studentDto.role( Enum.valueOf( Role.class, args.get( "role" ) ) );
        }
        if ( args.containsKey( "password" ) ) {
            studentDto.password( args.get( "password" ) );
        }
        if ( args.containsKey( "blocked" ) ) {
            studentDto.blocked( Boolean.parseBoolean( args.get( "blocked" ) ) );
        }
        if ( args.containsKey( "academyName" ) ) {
            studentDto.academyName( args.get( "academyName" ) );
        }
        if ( args.containsKey( "studyYear" ) ) {
            studentDto.studyYear( Integer.parseInt( args.get( "studyYear" ) ) );
        }
        if ( args.containsKey( "studyField" ) ) {
            studentDto.studyField( args.get( "studyField" ) );
        }
        if ( args.containsKey( "scholarship" ) ) {
            studentDto.scholarship( new BigDecimal( args.get( "scholarship" ) ) );
        }

        return studentDto.build();
    }
}
