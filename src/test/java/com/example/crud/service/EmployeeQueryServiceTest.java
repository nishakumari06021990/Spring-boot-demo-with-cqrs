package com.example.crud.service;

import com.example.crud.entity.Employee;
import com.example.crud.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
public class EmployeeQueryServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeQueryService employeeQueryService;

    private Employee employee1;
    private Employee employee2;

    @BeforeEach
    public void setUp() {
        employee1 = new Employee(1L, "John Doe", "Software Engineer");
        employee2 = new Employee(2L, "Jane Smith", "Product Manager");
    }

    @Test
    public void testGetEmployee_Success() {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee1));  // Mock the repository to return employee1 when the id is passed

        Employee result = employeeQueryService.getEmployee(1L);

        assertNotNull(result);
        assertEquals(1L, result.getEmployeeId());
        assertEquals("John Doe", result.getEmployeeName());
        assertEquals("Software Engineer", result.getEmployeeDescription());

        verify(employeeRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testGetEmployee_NotFound() {
        // Mock repository to return empty for any ID
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

        Employee result = employeeQueryService.getEmployee(1L);

        // The result should be null if the employee is not found
        assertNull(result);

        // Verify that the repository's findById method was called exactly once
        verify(employeeRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testGetAllEmployees() {
       when(employeeRepository.findAll()).thenReturn(Arrays.asList(employee1, employee2));

        List<Employee> employees = employeeQueryService.getAllEmployees();

        assertNotNull(employees);
        assertEquals(2, employees.size());
        assertTrue(employees.contains(employee1));
        assertTrue(employees.contains(employee2));

        // Verify that the repository's findAll method was called exactly once
        verify(employeeRepository, times(1)).findAll();
    }
}
