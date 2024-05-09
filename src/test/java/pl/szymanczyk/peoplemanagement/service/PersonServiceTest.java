package pl.szymanczyk.peoplemanagement.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.szymanczyk.peoplemanagement.mapper.PersonMapper;
import pl.szymanczyk.peoplemanagement.model.Person;
import pl.szymanczyk.peoplemanagement.model.PersonDto;
import pl.szymanczyk.peoplemanagement.model.PersonFilter;
import pl.szymanczyk.peoplemanagement.model.PersonRequestDto;
import pl.szymanczyk.peoplemanagement.model.student.Student;
import pl.szymanczyk.peoplemanagement.repository.PersonRepository;
import pl.szymanczyk.peoplemanagement.service.strategy.PersonStrategy;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonMapper personMapper;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PersonService personService;

    @Test
    void findAllByFilter_WithStrategyForType_ShouldReturnFilteredPage() {
        String type = "STUDENT";
        PersonFilter filter = new PersonFilter();
        Pageable pageable = Pageable.unpaged();
        Page<Person> expectedPage = mock(Page.class);

        PersonStrategy personStrategy = mock(PersonStrategy.class);

        Map<String, PersonStrategy> strategies = new HashMap<>();
        strategies.put(type, personStrategy);

        PersonService personService = new PersonService(personMapper, personRepository, strategies, passwordEncoder);

        when(personStrategy.findAllByFilter(filter, pageable)).thenReturn(expectedPage);

        Page<Person> resultPage = personService.findAllByFilter(filter, type, pageable);

        assertNotNull(resultPage);
        assertEquals(expectedPage, resultPage);
        verify(personStrategy, times(1)).findAllByFilter(filter, pageable);
        verifyNoInteractions(personRepository);
    }

    @Test
    void findAllByFilter_WithValidFilterAndType_ShouldReturnFilteredPage() {
        String type = "STUDENT";
        PersonFilter filter = new PersonFilter();
        filter.setFirstName("John");
        filter.setLastName("Doe");
        Pageable pageable = Pageable.unpaged();
        Page<Person> expectedPage = mock(Page.class);

        PersonStrategy personStrategy = mock(PersonStrategy.class);
        Map<String, PersonStrategy> strategies = new HashMap<>();
        strategies.put(type, personStrategy);

        PersonService personService = new PersonService(personMapper, personRepository, strategies, passwordEncoder);

        when(personStrategy.findAllByFilter(filter, pageable)).thenReturn(expectedPage);

        Page<Person> resultPage = personService.findAllByFilter(filter, type, pageable);

        assertNotNull(resultPage);
        assertEquals(expectedPage, resultPage);
        verify(personStrategy, times(1)).findAllByFilter(filter, pageable);
        verifyNoInteractions(personRepository);
    }

    @Test
    void findAllByFilter_WithNullFilterAndNullType_ShouldReturnAllPersons() {
        Pageable pageable = Pageable.unpaged();
        Page<Person> expectedPage = mock(Page.class);

        when(personRepository.findAll(pageable)).thenReturn(expectedPage);

        Page<Person> resultPage = personService.findAllByFilter(null, null, pageable);

        assertEquals(expectedPage, resultPage);
        verify(personRepository, times(1)).findAll(pageable);
    }

    @Test
    void save_WithValidPersonRequestDto_ShouldSavePerson() {
        String type = "STUDENT";
        PersonRequestDto personRequestDto = new PersonRequestDto();
        personRequestDto.setType(type);
        personRequestDto.setArgs(new HashMap<>());
        personRequestDto.getArgs().put("mail", "test@example.com");
        personRequestDto.getArgs().put("personalId", "1234567890");
        personRequestDto.getArgs().put("password", "password");

        PersonStrategy personStrategy = mock(PersonStrategy.class);
        Map<String, PersonStrategy> strategies = new HashMap<>();
        strategies.put(type, personStrategy);

        PersonMapper personMapper = mock(PersonMapper.class);
        PersonRepository personRepository = mock(PersonRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

        PersonService personService = new PersonService(personMapper, personRepository, strategies, passwordEncoder);

        Person savedPerson = new Student();
        savedPerson.setId(1L);

        when(personRepository.existsByMailOrPersonalId(anyString(), anyString())).thenReturn(false);
        when(personStrategy.save(personRequestDto)).thenReturn(savedPerson);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(personMapper.mapToPersonDto(savedPerson)).thenReturn(new PersonDto());

        PersonDto result = personService.save(personRequestDto);

        assertNotNull(result);
        verify(personRepository, times(1)).existsByMailOrPersonalId(anyString(), anyString());
        verify(personStrategy, times(1)).save(personRequestDto);
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(personMapper, times(1)).mapToPersonDto(savedPerson);
    }

    @Test
    void save_WithExistingPerson_ShouldThrowEntityExistsException() {
        String type = "STUDENT";
        PersonRequestDto personRequestDto = new PersonRequestDto();
        personRequestDto.setType(type);
        personRequestDto.setArgs(new HashMap<>());
        personRequestDto.getArgs().put("mail", "test@example.com");
        personRequestDto.getArgs().put("personalId", "1234567890");
        personRequestDto.getArgs().put("password", "password");

        Map<String, PersonStrategy> strategies = new HashMap<>();
        strategies.put(type, mock(PersonStrategy.class));

        PersonMapper personMapper = mock(PersonMapper.class);
        PersonRepository personRepository = mock(PersonRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

        PersonService personService = new PersonService(personMapper, personRepository, strategies, passwordEncoder);

        when(personRepository.existsByMailOrPersonalId(anyString(), anyString())).thenReturn(true);

        assertThrows(EntityExistsException.class, () -> {
            personService.save(personRequestDto);
        });
    }


    @Test
    void save_WithExistingEmailOrPersonalId_ShouldThrowEntityExistsException() {
        String type = "VALID_TYPE";
        PersonRequestDto personRequestDto = new PersonRequestDto();
        personRequestDto.setType(type);
        personRequestDto.setArgs(new HashMap<>());
        personRequestDto.getArgs().put("mail", "test@example.com");
        personRequestDto.getArgs().put("personalId", "1234567890");
        personRequestDto.getArgs().put("password", "password");

        PersonStrategy personStrategy = mock(PersonStrategy.class);
        Map<String, PersonStrategy> strategies = new HashMap<>();
        strategies.put(type, personStrategy);

        PersonMapper personMapper = mock(PersonMapper.class);
        PersonRepository personRepository = mock(PersonRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

        PersonService personService = new PersonService(personMapper, personRepository, strategies, passwordEncoder);

        when(personRepository.existsByMailOrPersonalId(anyString(), anyString())).thenReturn(true);

        assertThrows(EntityExistsException.class, () -> {
            personService.save(personRequestDto);
        });
    }

    @Test
    void update_SuccessfulUpdate_ShouldReturnUpdatedPersonDto() {
        Long id = 1L;
        String type = "STUDENT";
        PersonRequestDto personRequestDto = new PersonRequestDto();
        personRequestDto.setType(type);
        PersonDto expectedDto = new PersonDto();

        PersonStrategy personStrategy = mock(PersonStrategy.class);
        when(personStrategy.update(anyLong(), any(PersonRequestDto.class))).thenReturn(new Student());

        Map<String, PersonStrategy> strategies = new HashMap<>();
        strategies.put(type, personStrategy);

        PersonMapper personMapper = mock(PersonMapper.class);
        when(personMapper.mapToPersonDto(any(Person.class))).thenReturn(expectedDto);

        PersonService personService = new PersonService(personMapper, null, strategies, null);

        PersonDto resultDto = personService.update(id, personRequestDto);
        assertEquals(expectedDto, resultDto);
    }

    @Test
    void update_EmptyType_ShouldThrowEntityNotFoundException() {
        Long id = 1L;
        String type = "";
        PersonRequestDto personRequestDto = new PersonRequestDto();
        personRequestDto.setType(type);

        PersonStrategy personStrategy = mock(PersonStrategy.class);
        Map<String, PersonStrategy> strategies = new HashMap<>();
        strategies.put(type.toUpperCase(), personStrategy);

        PersonService personService = new PersonService(null, null, strategies, null);

        when(personStrategy.update(anyLong(), any(PersonRequestDto.class)))
                .thenThrow(new EntityNotFoundException("Invalid type"));

        assertThrows(EntityNotFoundException.class, () -> {
            personService.update(id, personRequestDto);
        });
    }

    @Test
    void update_MissingData_ShouldThrowIllegalArgumentException() {
        Long id = 1L;
        String type = "STUDENT";
        PersonRequestDto personRequestDto = new PersonRequestDto();
        personRequestDto.setType(type);

        PersonStrategy personStrategy = mock(PersonStrategy.class);
        Map<String, PersonStrategy> strategies = new HashMap<>();
        strategies.put(type.toUpperCase(), personStrategy);

        PersonService personService = new PersonService(null, null, strategies, null);
        when(personStrategy.update(anyLong(), any(PersonRequestDto.class)))
                .thenThrow(new EntityNotFoundException("Invalid type"));


        assertThrows(EntityNotFoundException.class, () -> {
            personService.update(id, personRequestDto);
        });
    }


    @Test
    void update_UnsuccessfulUpdate_ShouldThrowRuntimeException() {
        Long id = 1L;
        String type = "STUDENT";
        PersonRequestDto personRequestDto = new PersonRequestDto();
        personRequestDto.setType(type);

        PersonStrategy personStrategy = mock(PersonStrategy.class);
        when(personStrategy.update(anyLong(), any(PersonRequestDto.class))).thenThrow(new RuntimeException());

        Map<String, PersonStrategy> strategies = new HashMap<>();
        strategies.put(type, personStrategy);

        PersonService personService = new PersonService(null, null, strategies, null);

        assertThrows(RuntimeException.class, () -> {
            personService.update(id, personRequestDto);
        });
    }
}