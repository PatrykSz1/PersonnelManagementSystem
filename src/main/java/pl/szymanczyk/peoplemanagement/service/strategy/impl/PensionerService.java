package pl.szymanczyk.peoplemanagement.service.strategy.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import pl.szymanczyk.peoplemanagement.mapper.PensionerMapper;
import pl.szymanczyk.peoplemanagement.model.Person;
import pl.szymanczyk.peoplemanagement.model.PersonFilter;
import pl.szymanczyk.peoplemanagement.model.PersonRequestDto;
import pl.szymanczyk.peoplemanagement.model.pensioner.Pensioner;
import pl.szymanczyk.peoplemanagement.model.pensioner.PensionerDto;
import pl.szymanczyk.peoplemanagement.repository.PensionerRepository;
import pl.szymanczyk.peoplemanagement.service.strategy.PersonStrategy;

@Service("PENSIONER")
@RequiredArgsConstructor
@Slf4j
public class PensionerService implements PersonStrategy {

    private final PensionerRepository pensionerRepository;
    private final PensionerMapper pensionerMapper;

    @Override
    public Page<Person> findAllByFilter(PersonFilter filter, Pageable pageable) {
        if (filter != null) {
            return pensionerRepository.findAll(filter.buildSpecification(), pageable);
        } else {
            return pensionerRepository.findAllPensioner(pageable);
        }
    }

    @Override
    public Person save(PersonRequestDto personRequestDto) {
        return pensionerRepository.save(pensionerMapper.mapToEntity(personRequestDto.getArgs()));
    }

    @Override
    @Transactional
    @Modifying
    public Person update(Long personId, PersonRequestDto updatedPersonData) {
        Pensioner pensioner = pensionerRepository.findWithLockingById(personId)
                .orElseThrow(() -> new EntityNotFoundException("Pensioner not found with ID: " + personId));
        updatePensionerData(pensioner, updatedPersonData);
        return pensionerRepository.save(pensioner);
    }

    private void updatePensionerData(Pensioner pensioner, PersonRequestDto updatedPersonData) {
        PensionerDto pensionerDto = pensionerMapper.mapToDto(updatedPersonData.getArgs());
        pensioner.setPension(pensionerDto.getPension());
        pensioner.setEmploymentYears(pensionerDto.getEmploymentYears());
    }
}
