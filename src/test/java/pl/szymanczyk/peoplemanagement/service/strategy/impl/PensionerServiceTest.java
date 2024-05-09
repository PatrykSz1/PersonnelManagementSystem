package pl.szymanczyk.peoplemanagement.service.strategy.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import pl.szymanczyk.peoplemanagement.mapper.PensionerMapper;
import pl.szymanczyk.peoplemanagement.model.Person;
import pl.szymanczyk.peoplemanagement.model.PersonFilter;
import pl.szymanczyk.peoplemanagement.model.PersonRequestDto;
import pl.szymanczyk.peoplemanagement.model.pensioner.Pensioner;
import pl.szymanczyk.peoplemanagement.model.pensioner.PensionerDto;
import pl.szymanczyk.peoplemanagement.repository.PensionerRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PensionerServiceTest {

    @Mock
    private PensionerMapper pensionerMapper;

    @Mock
    private PensionerRepository pensionerRepository;

    @InjectMocks
    private PensionerService pensionerService;

    private Pensioner pensioner() {
        return Pensioner.builder()
                .id(1L)
                .firstName("test")
                .lastName("testowy")
                .mail("testowy@mail.com")
                .personalId("11111111111")
                .height(180)
                .weight(80)
                .password("password")
                .blocked(false)
                .pension(3500)
                .employmentYears(25)
                .build();
    }

    @Test
    void testFindAllByFilter_ShouldReturnPensionersList() {
        List<Pensioner> pensioners = new ArrayList<>();
        Pensioner pensioner1 = Pensioner.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .pension(1500)
                .employmentYears(25)
                .build();
        Pensioner pensioner2 = Pensioner.builder()
                .id(2L)
                .firstName("Alice")
                .lastName("Smith")
                .pension(1800)
                .employmentYears(30)
                .build();
        pensioners.add(pensioner1);
        pensioners.add(pensioner2);

        PersonFilter filter = new PersonFilter();
        filter.setPensionerMinPension(1500);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Pensioner> page = new PageImpl<>(pensioners);

        when(pensionerRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);

        Page<Person> resultPage = pensionerService.findAllByFilter(filter, pageable);
        assertEquals(2, resultPage.getTotalElements());
        assertTrue(resultPage.getContent().contains(pensioner1));
        assertTrue(resultPage.getContent().contains(pensioner2));
    }

    @Test
    void findAllByFilter_ShouldReturnAllPensionersWithoutFilter() {
        Pageable pageable = Pageable.unpaged();
        List<Pensioner> pensioners = Collections.singletonList(pensioner());

        Page<Pensioner> pensionerPage = new PageImpl<>(pensioners);

        when(pensionerRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pensionerPage);

        Page<Person> result = pensionerService.findAllByFilter(new PersonFilter(), pageable);

        assertEquals(pensioners.size(), result.getContent().size());
        assertEquals(pensioners, result.getContent());
        verify(pensionerRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void testSave_ShouldSavePensioner() {
        PersonRequestDto personRequestDto = new PersonRequestDto();
        personRequestDto.setType("pensioner");
        personRequestDto.setArgs(new HashMap<>());
        personRequestDto.getArgs().put("firstName", "John");
        personRequestDto.getArgs().put("lastName", "Doe");
        personRequestDto.getArgs().put("pension", "1500");
        personRequestDto.getArgs().put("employmentYears", "25");

        Pensioner pensionerToSave = Pensioner.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .pension(1500)
                .employmentYears(25)
                .build();

        when(pensionerMapper.mapToEntity(personRequestDto.getArgs())).thenReturn(pensionerToSave);
        when(pensionerRepository.save(pensionerToSave)).thenReturn(pensionerToSave);

        Person savedPerson = pensionerService.save(personRequestDto);

        assertNotNull(savedPerson);
        assertEquals(pensionerToSave, savedPerson);
        verify(pensionerRepository, times(1)).save(pensionerToSave);
    }

    @Test
    void testUpdate_ShouldUpdatePensioner() {
        Long personId = 1L;
        PersonRequestDto updatedPersonData = new PersonRequestDto();
        updatedPersonData.setType("pensioner");
        updatedPersonData.setArgs(new HashMap<>());
        updatedPersonData.getArgs().put("pension", "2000");

        Pensioner existingPensioner = Pensioner.builder()
                .id(personId)
                .firstName("John")
                .lastName("Doe")
                .pension(1500)
                .employmentYears(25)
                .build();

        PensionerDto pensionerDto = new PensionerDto();
        pensionerDto.setPension(2000);
        when(pensionerMapper.mapToDto(updatedPersonData.getArgs())).thenReturn(pensionerDto);

        when(pensionerRepository.findWithLockingById(personId)).thenReturn(Optional.of(existingPensioner));
        when(pensionerRepository.save(existingPensioner)).thenReturn(existingPensioner);

        Pensioner updatedPensioner = (Pensioner) pensionerService.update(personId, updatedPersonData);

        assertNotNull(updatedPensioner);
        assertEquals(2000, updatedPensioner.getPension());

        verify(pensionerRepository, times(1)).findWithLockingById(personId);
        verify(pensionerRepository, times(1)).save(existingPensioner);
    }

    @Test
    void testUpdate_ShouldThrowEntityNotFoundExceptionWithCorrectMessage() {
        Long personId = 1L;
        PersonRequestDto updatedPersonData = new PersonRequestDto();
        updatedPersonData.setType("pensioner");
        updatedPersonData.setArgs(new HashMap<>());
        updatedPersonData.getArgs().put("pension", "2000");

        when(pensionerRepository.findWithLockingById(personId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            pensionerService.update(personId, updatedPersonData);
        });

        verify(pensionerRepository, times(1)).findWithLockingById(personId);
        verify(pensionerRepository, never()).save(any());

        assertEquals("Pensioner not found with ID: " + personId, exception.getMessage());
    }
}