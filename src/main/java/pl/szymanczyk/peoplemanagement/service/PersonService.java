package pl.szymanczyk.peoplemanagement.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.szymanczyk.peoplemanagement.mapper.PersonMapper;
import pl.szymanczyk.peoplemanagement.model.Person;
import pl.szymanczyk.peoplemanagement.model.PersonDto;
import pl.szymanczyk.peoplemanagement.model.PersonFilter;
import pl.szymanczyk.peoplemanagement.model.PersonRequestDto;
import pl.szymanczyk.peoplemanagement.repository.PersonRepository;
import pl.szymanczyk.peoplemanagement.service.strategy.PersonStrategy;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonService {

    private final PersonMapper personMapper;
    private final PersonRepository personRepository;
    private final Map<String, PersonStrategy> strategies;
    private final PasswordEncoder passwordEncoder;

    public Page<Person> findAllByFilter(PersonFilter filter, String type, Pageable pageable) {
        return Optional.ofNullable(type).map(String::toUpperCase).map(uppercaseType -> {
            log.info("Type is not null. Checking strategy for type: {}", uppercaseType);
            if (strategies.containsKey(uppercaseType)) {
                log.info("Strategy found for type: {}", uppercaseType);
                PersonStrategy strategy = strategies.get(uppercaseType);
                return strategy.findAllByFilter(filter, pageable);
            } else {
                throw new EntityNotFoundException("No strategy found for type: " + uppercaseType);
            }
        }).orElseGet(() -> {
            if (filter != null && filter.isValidField()) {
                log.info("Filter is not null and valid. Building specification...");
                return personRepository.findAll(filter.buildSpecification(), pageable);
            } else {
                log.warn("Both type and filter are null or invalid. Returning all persons.");
                return personRepository.findAll(pageable);
            }
        });
    }

    @Transactional
    public PersonDto save(PersonRequestDto personRequestDto) {
        String type = personRequestDto.getType().toUpperCase();
        if (!strategies.containsKey(type)) {
            throw new EntityNotFoundException("Invalid type. Allowed values are: " + String.join(", ", strategies.keySet()));
        }
        if (personRepository.existsByMailOrPersonalId(personRequestDto.getArgs().get("mail"), personRequestDto.getArgs().get("personalId"))) {
            throw new EntityExistsException("Person with this email or this personal ID already exists.");
        }
        PersonStrategy strategy = strategies.get(type);
        Person savedPerson = strategy.save(personRequestDto);
        String encodedPassword = passwordEncoder.encode(personRequestDto.getArgs().get("password"));
        savedPerson.setPassword(encodedPassword);
        return personMapper.mapToPersonDto(savedPerson);
    }

    @Transactional
    @Modifying
    public PersonDto update(Long id, PersonRequestDto personRequestDto) {
        PersonStrategy strategy = strategies.get(personRequestDto.getType().toUpperCase());
        Person updatePerson = strategy.update(id, personRequestDto);
        return personMapper.mapToPersonDto(updatePerson);
    }
}


