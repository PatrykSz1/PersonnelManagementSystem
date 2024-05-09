package pl.szymanczyk.peoplemanagement.service.strategy;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.szymanczyk.peoplemanagement.mapper.EmployeePositionMapper;
import pl.szymanczyk.peoplemanagement.model.employeeposition.EmployeePosition;
import pl.szymanczyk.peoplemanagement.model.employeeposition.EmployeePositionDto;
import pl.szymanczyk.peoplemanagement.repository.EmployeePositionRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeePositionServiceTest {

    @Mock
    private EmployeePositionRepository employeePositionRepository;

    @Mock
    private EmployeePositionMapper employeePositionMapper;

    @InjectMocks
    private EmployeePositionService employeePositionService;

    @Test
    void testCreateEmployeePosition_ShouldCreateAndReturnEmployeePositionDto() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusYears(1);

        EmployeePositionDto employeePositionDto = EmployeePositionDto.builder()
                .id(1)
                .startDate(startDate)
                .endDate(endDate)
                .position("Software Developer")
                .salary(BigDecimal.valueOf(5000))
                .build();

        EmployeePosition employeePosition = EmployeePosition.builder()
                .id(1L)
                .startDate(startDate)
                .endDate(endDate)
                .position("Software Developer")
                .salary(BigDecimal.valueOf(5000))
                .build();

        when(employeePositionMapper.toEntity(employeePositionDto)).thenReturn(employeePosition);
        when(employeePositionRepository.save(employeePosition)).thenReturn(employeePosition);
        when(employeePositionMapper.toDto(employeePosition)).thenReturn(employeePositionDto);

        EmployeePositionDto createdEmployeePositionDto = employeePositionService.createEmployeePosition(employeePositionDto);

        assertNotNull(createdEmployeePositionDto);
        assertEquals(employeePositionDto.getId(), createdEmployeePositionDto.getId());
        assertEquals(employeePositionDto.getStartDate(), createdEmployeePositionDto.getStartDate());
        assertEquals(employeePositionDto.getEndDate(), createdEmployeePositionDto.getEndDate());
        assertEquals(employeePositionDto.getPosition(), createdEmployeePositionDto.getPosition());
        assertEquals(employeePositionDto.getSalary(), createdEmployeePositionDto.getSalary());
        verify(employeePositionMapper, times(1)).toEntity(employeePositionDto);
        verify(employeePositionRepository, times(1)).save(employeePosition);
        verify(employeePositionMapper, times(1)).toDto(employeePosition);
    }

    @Test
    void testCreateEmployeePosition_ShouldThrowEntityNotFoundException() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusYears(1);

        EmployeePositionDto employeePositionDto = EmployeePositionDto.builder()
                .id(1)
                .startDate(startDate)
                .endDate(endDate)
                .position("Software Developer")
                .salary(BigDecimal.valueOf(5000))
                .build();

        EmployeePosition employeePosition = EmployeePosition.builder()
                .id(1L)
                .startDate(startDate)
                .endDate(endDate)
                .position("Software Developer")
                .salary(BigDecimal.valueOf(5000))
                .build();

        when(employeePositionMapper.toEntity(employeePositionDto)).thenReturn(employeePosition);
        when(employeePositionRepository.save(employeePosition)).thenThrow(new EntityNotFoundException());

        assertThrows(EntityNotFoundException.class, () -> employeePositionService.createEmployeePosition(employeePositionDto));
        verify(employeePositionMapper, times(1)).toEntity(employeePositionDto);
        verify(employeePositionRepository, times(1)).save(employeePosition);
    }
}