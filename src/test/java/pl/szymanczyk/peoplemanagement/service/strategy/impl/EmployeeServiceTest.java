package pl.szymanczyk.peoplemanagement.service.strategy.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import pl.szymanczyk.peoplemanagement.mapper.EmployeeMapper;
import pl.szymanczyk.peoplemanagement.model.Person;
import pl.szymanczyk.peoplemanagement.model.PersonFilter;
import pl.szymanczyk.peoplemanagement.model.PersonRequestDto;
import pl.szymanczyk.peoplemanagement.model.employee.Employee;
import pl.szymanczyk.peoplemanagement.model.employee.EmployeeDto;
import pl.szymanczyk.peoplemanagement.model.employeeposition.EmployeePosition;
import pl.szymanczyk.peoplemanagement.repository.EmployeePositionRepository;
import pl.szymanczyk.peoplemanagement.repository.EmployeeRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeePositionRepository employeePositionRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee() {
        return Employee.builder()
                .id(1L)
                .firstName("test")
                .lastName("testowy")
                .mail("testowy@mail.com")
                .personalId("11111111111")
                .height(180)
                .weight(80)
                .password("password")
                .blocked(false)
                .startDateOfEmployment(LocalDate.now())
                .position("Software Developer")
                .salary(BigDecimal.valueOf(2000))
                .employeePositions(new HashSet<>())
                .build();
    }

    private Employee secondEmployee() {
        return Employee.builder()
                .id(2L)
                .firstName("Wiesiek")
                .lastName("Wieśkowy")
                .mail("wiesiek@mail.com")
                .personalId("22222222222")
                .height(200)
                .weight(55)
                .password("wiesiek123")
                .blocked(false)
                .startDateOfEmployment(LocalDate.now().plusMonths(1))
                .salary(BigDecimal.valueOf(10000))
                .position("CEO")
                .employeePositions(new HashSet<>())
                .build();
    }

    private EmployeePosition employeePosition() {
        return EmployeePosition.builder()
                .id(1L)
                .startDate(LocalDate.of(2022, 1, 1))
                .endDate(LocalDate.of(2023, 1, 1))
                .position("Hydraulik")
                .salary(BigDecimal.valueOf(2000))
                .build();
    }

    @Test
    void findAllByFilter_ShouldReturnPageOfFilterEmployee() {
        PersonFilter filter = new PersonFilter();
        filter.setPosition("Software Developer");
        filter.setMinSalary(BigDecimal.valueOf(1000));

        Pageable pageable = Pageable.unpaged();

        Employee employee = employee();

        List<Employee> employees = new ArrayList<>();
        employees.add(employee);
        Page<Employee> employeePage = mock(Page.class);


        when(employeePage.getContent()).thenReturn(employees);
        when(employeeRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(employeePage);
        Page<Person> result = employeeService.findAllByFilter(filter, pageable);

        assertEquals(1, result.getContent().size());
        assertEquals(result, employeePage);
        assertInstanceOf(Employee.class, result.getContent().get(0));
        assertEquals(employee.getPosition(), ((Employee) result.getContent().get(0)).getPosition());
        assertEquals(employee.getSalary(), ((Employee) result.getContent().get(0)).getSalary());
        verify(employeeRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAllByFilter_ShouldReturnAllEmployeesWithoutFilter() {
        Pageable pageable = Pageable.unpaged();
        List<Employee> allEmployees = Arrays.asList(employee(), secondEmployee());

        Page<Employee> employeePage = new PageImpl<>(allEmployees);

        when(employeeRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(employeePage);

        Page<Person> result = employeeService.findAllByFilter(new PersonFilter(), pageable);

        assertEquals(allEmployees.size(), result.getContent().size());
        assertEquals(allEmployees, result.getContent());
        verify(employeeRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAllByFilter_ShouldReturnEmptyPageWhenNoMatchingEmployees() {
        PersonFilter filter = new PersonFilter();
        filter.setPosition("Manager");
        filter.setMinSalary(BigDecimal.valueOf(1500));

        Pageable pageable = Pageable.unpaged();
        List<Employee> employees = Collections.emptyList();

        Page<Employee> emptyEmployeePage = new PageImpl<>(employees);

        when(employeeRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(emptyEmployeePage);

        Page<Person> result = employeeService.findAllByFilter(filter, pageable);

        assertTrue(result.isEmpty());
        verify(employeeRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void save_ShouldSaveEmployee() {
        PersonRequestDto personRequestDto = mock(PersonRequestDto.class);
        Employee employeeToSave = new Employee();

        when(employeeMapper.mapToEntity(personRequestDto.getArgs())).thenReturn(employeeToSave);
        when(employeeRepository.save(employeeToSave)).thenReturn(employeeToSave);

        Person savedPerson = employeeService.save(personRequestDto);

        assertNotNull(savedPerson);
        assertEquals(employeeToSave, savedPerson);
        verify(employeeRepository, times(1)).save(employeeToSave);
    }

    @Test
    void addPositionToEmployee() {
        Employee employee = employee();
        EmployeePosition employeePosition = employeePosition();

        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(employeePositionRepository.findById(employeePosition.getId())).thenReturn(Optional.of(employeePosition));
        when(employeePositionRepository.existsByEmployeeAndEndDateAfterAndStartDateBefore(employee, employeePosition.getEndDate(), employeePosition.getStartDate())).thenReturn(false);
        when(employeeMapper.toDto(employee)).thenReturn(new EmployeeDto());

        EmployeeDto result = employeeService.addPositionToEmployee(employee.getId(), employeePosition.getId());

        assertNotNull(result);
        verify(employeeRepository, times(1)).findById(employee.getId());
        verify(employeePositionRepository, times(1)).findById(employeePosition.getId());
        verify(employeePositionRepository, times(1)).existsByEmployeeAndEndDateAfterAndStartDateBefore(employee, employeePosition.getEndDate(), employeePosition.getStartDate());
        verify(employeeMapper, times(1)).toDto(employee);

    }

    @Test
    void addPositionToEmployee_ShouldThrowEntityExistsExceptionWhenPositionDatesOverlap() {
        Employee employee = employee();
        EmployeePosition employeePosition = employeePosition();

        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(employeePositionRepository.findById(employeePosition.getId())).thenReturn(Optional.of(employeePosition));
        when(employeePositionRepository.existsByEmployeeAndEndDateAfterAndStartDateBefore(employee, employeePosition.getEndDate(), employeePosition.getStartDate())).thenReturn(true);

        EntityExistsException exception = assertThrows(EntityExistsException.class, () -> {
            employeeService.addPositionToEmployee(employee.getId(), employeePosition.getId());
        });

        String expectedMessage = "New position dates overlap with existing positions";
        assertEquals(expectedMessage, exception.getMessage());

        verify(employeeRepository, times(1)).findById(employee.getId());
        verify(employeePositionRepository, times(1)).findById(employeePosition.getId());
        verify(employeePositionRepository, times(1)).existsByEmployeeAndEndDateAfterAndStartDateBefore(employee, employeePosition.getEndDate(), employeePosition.getStartDate());
    }

    @Test
    void addPositionToEmployee_ShouldAddNewPositionWhenNoExistingPositionsOverlap() {
        Employee employee = employee();
        EmployeePosition existingPosition = employeePosition();
        LocalDate newStartDate = existingPosition.getEndDate().plusDays(1);
        LocalDate newEndDate = newStartDate.plusYears(1);
        EmployeePosition newPosition = EmployeePosition.builder()
                .id(3L)
                .startDate(newStartDate)
                .endDate(newEndDate)
                .position("New Position")
                .salary(BigDecimal.valueOf(6000))
                .build();

        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(employeePositionRepository.findById(newPosition.getId())).thenReturn(Optional.of(newPosition));
        when(employeePositionRepository.existsByEmployeeAndEndDateAfterAndStartDateBefore(employee, newPosition.getEndDate(), newPosition.getStartDate())).thenReturn(false);
        when(employeeMapper.toDto(employee)).thenReturn(new EmployeeDto());

        EmployeeDto result = employeeService.addPositionToEmployee(employee.getId(), newPosition.getId());

        assertNotNull(result);
        verify(employeeRepository, times(1)).findById(employee.getId());
        verify(employeePositionRepository, times(1)).findById(newPosition.getId());
        verify(employeePositionRepository, times(1)).existsByEmployeeAndEndDateAfterAndStartDateBefore(employee, newPosition.getEndDate(), newPosition.getStartDate());
        verify(employeeMapper, times(1)).toDto(employee);
    }

    @Test
    void addPositionToEmployee_ShouldThrowEntityNotFoundExceptionWhenEmployeeNotFound() {
        Long nonExistentEmployeeId = 999L;
        EmployeePosition newPosition = new EmployeePosition();
        newPosition.setId(1L);

        when(employeeRepository.findById(nonExistentEmployeeId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            employeeService.addPositionToEmployee(nonExistentEmployeeId, newPosition.getId());
        });

        verify(employeeRepository, times(1)).findById(nonExistentEmployeeId);
        verify(employeePositionRepository, never()).findById(anyLong());
        verify(employeePositionRepository, never()).existsByEmployeeAndEndDateAfterAndStartDateBefore(any(Employee.class), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void addPositionToEmployee_ShouldThrowEntityNotFoundExceptionWhenPositionNotFound() {
        Employee existingEmployee = new Employee();
        existingEmployee.setId(1L);
        Long nonExistentPositionId = 999L;

        when(employeeRepository.findById(existingEmployee.getId())).thenReturn(Optional.of(existingEmployee));
        when(employeePositionRepository.findById(nonExistentPositionId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            employeeService.addPositionToEmployee(existingEmployee.getId(), nonExistentPositionId);
        });

        verify(employeeRepository, times(1)).findById(existingEmployee.getId());
        verify(employeePositionRepository, times(1)).findById(nonExistentPositionId);
        verify(employeePositionRepository, never()).existsByEmployeeAndEndDateAfterAndStartDateBefore(any(Employee.class), any(LocalDate.class), any(LocalDate.class));
    }


    @Test
    @Transactional
    void testUpdate_ShouldThrowEntityNotFoundException() {
        long personId = employee().getId();
        PersonRequestDto updatedPersonData = new PersonRequestDto();
        updatedPersonData.setType("employee");
        updatedPersonData.setArgs(new HashMap<>());
        updatedPersonData.getArgs().put("position", "Senior Software Developer");
        updatedPersonData.getArgs().put("startDateOfEmployment", "2024-02-24");
        updatedPersonData.getArgs().put("salary", "5000");

        when(employeeRepository.findWithLockingById(personId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            employeeService.update(personId, updatedPersonData);
        });

        verify(employeeRepository, times(1)).findWithLockingById(personId);
        verify(employeeRepository, never()).save(any());

        assertEquals("Employee not found with ID: " + personId, exception.getMessage());
    }

    private EmployeeDto createEmployeeDto() {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setPosition("Senior Software Developer");
        employeeDto.setStartDateOfEmployment(LocalDate.parse("2024-02-24"));
        employeeDto.setSalary(BigDecimal.valueOf(5000));
        return employeeDto;
    }
}