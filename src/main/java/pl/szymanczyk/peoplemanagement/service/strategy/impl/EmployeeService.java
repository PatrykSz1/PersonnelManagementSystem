package pl.szymanczyk.peoplemanagement.service.strategy.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import pl.szymanczyk.peoplemanagement.mapper.EmployeeMapper;
import pl.szymanczyk.peoplemanagement.model.Person;
import pl.szymanczyk.peoplemanagement.model.PersonFilter;
import pl.szymanczyk.peoplemanagement.model.PersonRequestDto;
import pl.szymanczyk.peoplemanagement.model.employee.Employee;
import pl.szymanczyk.peoplemanagement.model.employee.EmployeeDto;
import pl.szymanczyk.peoplemanagement.model.employeeposition.EmployeePosition;
import pl.szymanczyk.peoplemanagement.repository.EmployeePositionRepository;
import pl.szymanczyk.peoplemanagement.repository.EmployeeRepository;
import pl.szymanczyk.peoplemanagement.service.strategy.PersonStrategy;

@Service("EMPLOYEE")
@RequiredArgsConstructor
@Slf4j
public class EmployeeService implements PersonStrategy {

    private final EmployeeRepository employeeRepository;
    private final EmployeePositionRepository employeePositionRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    public Page<Person> findAllByFilter(PersonFilter filter, Pageable pageable) {
        if (filter != null) {
            return employeeRepository.findAll(filter.buildSpecification(), pageable);
        } else {
            return employeeRepository.findAllEmployee(pageable);
        }
    }

    @Override
    public Person save(PersonRequestDto personRequestDto) {
        return employeeRepository.save(employeeMapper.mapToEntity(personRequestDto.getArgs()));
    }

    @Transactional
    public EmployeeDto addPositionToEmployee(Long employeeId, Long positionId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        EmployeePosition positionToAdd = employeePositionRepository.findById(positionId).orElseThrow(() -> new EntityNotFoundException("Employee Position not found with ID: " + positionId));

        if (employeePositionRepository.existsByEmployeeAndEndDateAfterAndStartDateBefore(employee, positionToAdd.getEndDate(), positionToAdd.getStartDate())) {
            throw new EntityExistsException("New position dates overlap with existing positions");
        }
        employee.getEmployeePositions().add(positionToAdd);

        return employeeMapper.toDto(employee);
    }

    @Override
    @Transactional
    @Modifying
    public Person update(Long personId, PersonRequestDto updatedPersonData) {
        Employee employee = employeeRepository.findWithLockingById(personId).
                orElseThrow(() -> new EntityNotFoundException("Employee not found with ID: " + personId));
        updateEmployeeData(employee, updatedPersonData);
        return employeeRepository.save(employee);
    }

    private void updateEmployeeData(Employee employee, PersonRequestDto updatedPersonData) {
        EmployeeDto employeeDto = employeeMapper.mapToDto(updatedPersonData.getArgs());
        employee.setPosition(employeeDto.getPosition());
        employee.setStartDateOfEmployment(employeeDto.getStartDateOfEmployment());
        employee.setSalary(employeeDto.getSalary());
    }
}
