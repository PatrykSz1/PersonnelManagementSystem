package pl.szymanczyk.peoplemanagement.service.strategy;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.szymanczyk.peoplemanagement.mapper.EmployeePositionMapper;
import pl.szymanczyk.peoplemanagement.model.employeeposition.EmployeePosition;
import pl.szymanczyk.peoplemanagement.model.employeeposition.EmployeePositionDto;
import pl.szymanczyk.peoplemanagement.repository.EmployeePositionRepository;

@Service
@RequiredArgsConstructor
public class EmployeePositionService {

    private final EmployeePositionRepository employeePositionRepository;
    private final EmployeePositionMapper employeePositionMapper;

    public EmployeePositionDto createEmployeePosition(EmployeePositionDto employeePositionDto) {
        try {
            EmployeePosition employeePosition = employeePositionMapper.toEntity(employeePositionDto);
            EmployeePosition savedEmployeePosition = employeePositionRepository.save(employeePosition);
            return employeePositionMapper.toDto(savedEmployeePosition);
        } catch (Exception e) {
            throw new EntityNotFoundException("Error creating employee position", e);
        }
    }
}