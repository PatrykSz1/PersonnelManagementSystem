package pl.szymanczyk.peoplemanagement.service.strategy;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.szymanczyk.peoplemanagement.model.Person;
import pl.szymanczyk.peoplemanagement.model.PersonFilter;
import pl.szymanczyk.peoplemanagement.model.PersonRequestDto;


public interface PersonStrategy {

    Person save(PersonRequestDto personRequestDto);

    Page<Person> findAllByFilter(PersonFilter filter, Pageable pageable);

    Person update(Long personId, PersonRequestDto updatedPersonData);
}