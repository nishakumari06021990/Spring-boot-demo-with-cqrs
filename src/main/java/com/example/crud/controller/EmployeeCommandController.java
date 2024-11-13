package com.example.crud.controller;


import com.example.crud.commands.CreateEmployeeCommand;
import com.example.crud.commands.DeleteEmployeeCommand;
import com.example.crud.commands.UpdateEmployeeCommand;
import com.example.crud.entity.Employee;
import com.example.crud.service.EmployeeCommandHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employees")
@Tag(name = "Employee  Command controller", description = "Performs Employee insert , update and delete operations")
public class EmployeeCommandController
{
    @Autowired
    private EmployeeCommandHandler commandHandler;

    @Operation(summary = "Create Employee ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Save Employee",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Employee.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
   @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody CreateEmployeeCommand command) {
        return commandHandler.handle(command);
    }

    @Operation(summary = "Update Employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update Employee",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Employee.class))}),
            @ApiResponse(responseCode = "400", description = "Employee Not Found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody UpdateEmployeeCommand command) {
        command.setEmployeeId(id);
        return commandHandler.handle(command);
    }

    @Operation(summary = "Delete the Employee by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete the Employee",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = String.class))})
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        return commandHandler.handle(new DeleteEmployeeCommand(id));
    }


}