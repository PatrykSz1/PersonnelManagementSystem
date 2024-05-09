package pl.szymanczyk.peoplemanagement.mapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import javax.annotation.processing.Generated;
import pl.szymanczyk.peoplemanagement.model.Role;
import pl.szymanczyk.peoplemanagement.model.employee.Employee;
import pl.szymanczyk.peoplemanagement.model.employee.EmployeeDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-02-25T17:15:13+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 19.0.1 (Oracle Corporation)"
)
public class EmployeeMapperImpl implements EmployeeMapper {

    @Override
    public EmployeeDto mapToDto(Map<String, String> args) {
        if ( args == null ) {
            return null;
        }

        EmployeeDto.EmployeeDtoBuilder<?, ?> employeeDto = EmployeeDto.builder();

        if ( args.containsKey( "id" ) ) {
            employeeDto.id( Integer.parseInt( args.get( "id" ) ) );
        }
        if ( args.containsKey( "firstName" ) ) {
            employeeDto.firstName( args.get( "firstName" ) );
        }
        if ( args.containsKey( "lastName" ) ) {
            employeeDto.lastName( args.get( "lastName" ) );
        }
        if ( args.containsKey( "mail" ) ) {
            employeeDto.mail( args.get( "mail" ) );
        }
        if ( args.containsKey( "personalId" ) ) {
            employeeDto.personalId( args.get( "personalId" ) );
        }
        if ( args.containsKey( "height" ) ) {
            employeeDto.height( Integer.parseInt( args.get( "height" ) ) );
        }
        if ( args.containsKey( "weight" ) ) {
            employeeDto.weight( Integer.parseInt( args.get( "weight" ) ) );
        }
        if ( args.containsKey( "password" ) ) {
            employeeDto.password( args.get( "password" ) );
        }
        if ( args.containsKey( "blocked" ) ) {
            employeeDto.blocked( Boolean.parseBoolean( args.get( "blocked" ) ) );
        }
        if ( args.containsKey( "startDateOfEmployment" ) ) {
            employeeDto.startDateOfEmployment( LocalDate.parse( args.get( "startDateOfEmployment" ) ) );
        }
        if ( args.containsKey( "position" ) ) {
            employeeDto.position( args.get( "position" ) );
        }
        if ( args.containsKey( "salary" ) ) {
            employeeDto.salary( new BigDecimal( args.get( "salary" ) ) );
        }
        if ( args.containsKey( "role" ) ) {
            employeeDto.role( Enum.valueOf( Role.class, args.get( "role" ) ) );
        }

        return employeeDto.build();
    }

    @Override
    public Employee mapToEntity(Map<String, String> args) {
        if ( args == null ) {
            return null;
        }

        Employee.EmployeeBuilder<?, ?> employee = Employee.builder();

        if ( args.containsKey( "firstName" ) ) {
            employee.firstName( args.get( "firstName" ) );
        }
        if ( args.containsKey( "lastName" ) ) {
            employee.lastName( args.get( "lastName" ) );
        }
        if ( args.containsKey( "personalId" ) ) {
            employee.personalId( args.get( "personalId" ) );
        }
        if ( args.containsKey( "height" ) ) {
            employee.height( Integer.parseInt( args.get( "height" ) ) );
        }
        if ( args.containsKey( "weight" ) ) {
            employee.weight( Integer.parseInt( args.get( "weight" ) ) );
        }
        if ( args.containsKey( "mail" ) ) {
            employee.mail( args.get( "mail" ) );
        }
        if ( args.containsKey( "password" ) ) {
            employee.password( args.get( "password" ) );
        }
        if ( args.containsKey( "id" ) ) {
            employee.id( Long.parseLong( args.get( "id" ) ) );
        }
        if ( args.containsKey( "role" ) ) {
            employee.role( Enum.valueOf( Role.class, args.get( "role" ) ) );
        }
        if ( args.containsKey( "blocked" ) ) {
            employee.blocked( Boolean.parseBoolean( args.get( "blocked" ) ) );
        }
        if ( args.containsKey( "startDateOfEmployment" ) ) {
            employee.startDateOfEmployment( LocalDate.parse( args.get( "startDateOfEmployment" ) ) );
        }
        if ( args.containsKey( "position" ) ) {
            employee.position( args.get( "position" ) );
        }
        if ( args.containsKey( "salary" ) ) {
            employee.salary( new BigDecimal( args.get( "salary" ) ) );
        }

        return employee.build();
    }

    @Override
    public EmployeeDto toDto(Employee employee) {
        if ( employee == null ) {
            return null;
        }

        EmployeeDto.EmployeeDtoBuilder<?, ?> employeeDto = EmployeeDto.builder();

        employeeDto.id( (int) employee.getId() );
        employeeDto.firstName( employee.getFirstName() );
        employeeDto.lastName( employee.getLastName() );
        employeeDto.mail( employee.getMail() );
        employeeDto.personalId( employee.getPersonalId() );
        employeeDto.height( employee.getHeight() );
        employeeDto.weight( employee.getWeight() );
        employeeDto.password( employee.getPassword() );
        employeeDto.blocked( employee.isBlocked() );
        employeeDto.startDateOfEmployment( employee.getStartDateOfEmployment() );
        employeeDto.position( employee.getPosition() );
        employeeDto.salary( employee.getSalary() );
        employeeDto.role( employee.getRole() );

        return employeeDto.build();
    }
}
