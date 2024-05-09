package pl.szymanczyk.peoplemanagement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.szymanczyk.peoplemanagement.model.Person;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Page<Person> findAll(Specification<Person> personSpecification, Pageable pageable);

    Optional<Person> findByMail(String mail);

    boolean existsByMailOrPersonalId(String mail, String personalId);
}

