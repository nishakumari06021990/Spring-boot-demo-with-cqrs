package com.example.crud.service;

import com.example.crud.commands.CreateEmployeeCommand;
import com.example.crud.commands.UpdateEmployeeCommand;
import com.example.crud.commands.DeleteEmployeeCommand;
import com.example.crud.entity.Employee;
import com.example.crud.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmployeeCommandHandlerTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeProducer employeeProducer;

    @InjectMocks
    private EmployeeCommandHandler employeeCommandHandler;

    private CreateEmployeeCommand createEmployeeCommand;
    private UpdateEmployeeCommand updateEmployeeCommand;
    private DeleteEmployeeCommand deleteEmployeeCommand;
    private Employee employee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Sample data for testing
        employee = new Employee("John Doe", "Software Engineer");
        createEmployeeCommand = new CreateEmployeeCommand("John Doe", "Software Engineer");
        updateEmployeeCommand = new UpdateEmployeeCommand(1L, "John Doe", "Senior Software Engineer");
        deleteEmployeeCommand = new DeleteEmployeeCommand(1L);
    }

    @Test
    void testHandleCreateEmployee() {
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        ResponseEntity<Employee> response = employeeCommandHandler.handle(createEmployeeCommand);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John Doe", response.getBody().getEmployeeName());

        verify(employeeRepository, times(1)).save(any(Employee.class));
        verify(employeeProducer, times(1)).sendCreateMessage(any(Employee.class));
    }

    @Test
    void testHandleUpdateEmployee() {
        when(employeeRepository.findById(updateEmployeeCommand.getEmployeeId())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        ResponseEntity<Employee> response = employeeCommandHandler.handle(updateEmployeeCommand);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Senior Software Engineer", response.getBody().getEmployeeDescription());

        verify(employeeRepository, times(1)).findById(updateEmployeeCommand.getEmployeeId());
        verify(employeeRepository, times(1)).save(any(Employee.class));
        verify(employeeProducer, times(1)).sendUpdateMessage(any(Employee.class));
    }

    @Test
    void testHandleUpdateEmployeeNotFound() {
        when(employeeRepository.findById(updateEmployeeCommand.getEmployeeId())).thenReturn(Optional.empty());

        ResponseEntity<Employee> response = employeeCommandHandler.handle(updateEmployeeCommand);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(employeeRepository, times(1)).findById(updateEmployeeCommand.getEmployeeId());
        verify(employeeProducer, never()).sendUpdateMessage(any(Employee.class));
    }

    @Test
    void testHandleDeleteEmployee() {
        when(employeeRepository.existsById(deleteEmployeeCommand.getEmployeeId())).thenReturn(true);

        ResponseEntity<Void> response = employeeCommandHandler.handle(deleteEmployeeCommand);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(employeeRepository, times(1)).deleteById(deleteEmployeeCommand.getEmployeeId());
        verify(employeeProducer, times(1)).sendDeleteMessage(deleteEmployeeCommand.getEmployeeId());
    }

    @Test
    void testHandleDeleteEmployeeNotFound() {
        when(employeeRepository.existsById(deleteEmployeeCommand.getEmployeeId())).thenReturn(false);

        ResponseEntity<Void> response = employeeCommandHandler.handle(deleteEmployeeCommand);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(employeeRepository, times(1)).existsById(deleteEmployeeCommand.getEmployeeId());
        verify(employeeRepository, never()).deleteById(deleteEmployeeCommand.getEmployeeId());
        verify(employeeProducer, never()).sendDeleteMessage(anyLong());
    }
}

