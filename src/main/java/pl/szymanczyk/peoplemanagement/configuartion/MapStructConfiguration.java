package pl.szymanczyk.peoplemanagement.configuartion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.szymanczyk.peoplemanagement.mapper.*;

@Configuration
public class MapStructConfiguration {

    @Bean
    public StudentMapper studentMapper() {
        return new StudentMapperImpl();
    }

    @Bean
    public PensionerMapper pensionerMapper() {
        return new PensionerMapperImpl();
    }

    @Bean
    public EmployeeMapper employeeMapper() {
        return new EmployeeMapperImpl();
    }

    @Bean
    public PersonMapper personMapper() {
        return new PersonMapperImpl();
    }

    @Bean
    public EmployeePositionMapper employeePositionMapper(){
        return new EmployeePositionMapperImpl();
    }
}


