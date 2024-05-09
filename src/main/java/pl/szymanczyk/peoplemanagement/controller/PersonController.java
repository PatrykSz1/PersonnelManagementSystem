package pl.szymanczyk.peoplemanagement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.szymanczyk.peoplemanagement.mapper.PersonMapper;
import pl.szymanczyk.peoplemanagement.model.Person;
import pl.szymanczyk.peoplemanagement.model.PersonDto;
import pl.szymanczyk.peoplemanagement.model.PersonFilter;
import pl.szymanczyk.peoplemanagement.model.PersonRequestDto;
import pl.szymanczyk.peoplemanagement.model.employee.EmployeeDto;
import pl.szymanczyk.peoplemanagement.model.employeeposition.EmployeePositionDto;
import pl.szymanczyk.peoplemanagement.service.PersonService;
import pl.szymanczyk.peoplemanagement.service.strategy.EmployeePositionService;
import pl.szymanczyk.peoplemanagement.service.strategy.impl.EmployeeService;

@RestController
@RequestMapping("/api/v1/people")
@RequiredArgsConstructor
@Slf4j
public class PersonController {

    private final PersonService personService;
    private final PersonMapper personMapper;
    private final EmployeeService employeeService;
    private final EmployeePositionService employeePositionService;


    @GetMapping
    public ResponseEntity<Page<PersonDto>> findAllByFilter(
            @RequestParam(required = false) String type,
            @ModelAttribute PersonFilter filter,
            Pageable pageable) {

        log.info("Type: {}", type);
        log.info("Filter: {}", filter);

        Page<Person> persons = personService.findAllByFilter(filter, type, pageable);
        return new ResponseEntity<>(persons.map(personMapper::mapToPersonDto), HttpStatus.OK);
    }

    @PostMapping("/{employeeId}/positions/{positionId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<EmployeeDto> addPositionToEmployee(
            @PathVariable Long employeeId,
            @PathVariable Long positionId) {
        EmployeeDto updatedEmployee = employeeService.addPositionToEmployee(employeeId, positionId);
        return new ResponseEntity<>(updatedEmployee, HttpStatus.CREATED);
    }

    @PostMapping("/create-employee-position")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeePositionDto> createEmployeePosition(@RequestBody @Valid EmployeePositionDto employeePositionDto) {
        return new ResponseEntity<>(employeePositionService.createEmployeePosition(employeePositionDto), HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<PersonDto> save(@RequestBody @Valid PersonRequestDto personRequestDto) {
        PersonDto savedPerson = personService.save(personRequestDto);
        return new ResponseEntity<>(savedPerson, HttpStatus.CREATED);
    }

    @PutMapping("/{personId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PersonDto> updatePerson(
            @PathVariable("personId") Long personId,
            @RequestBody @Valid PersonRequestDto personRequestDto) {
        PersonDto updatedPerson = personService.update(personId, personRequestDto);
        return new ResponseEntity<>(updatedPerson, HttpStatus.OK);
    }
}