package com.example.employee.controller;

import com.example.employee.model.Employee;
import com.example.employee.service.EmployeeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<?> createEmployee(@Valid @RequestBody Employee employee) {
        try {
            Employee createdEmployee = employeeService.createEmployee(employee);
            return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error creating employee: " + e.getMessage());
        }
    }

    @GetMapping()
    private ResponseEntity<List<Employee>> getAllEmployeeDetails() {
        return new ResponseEntity<>(employeeService.getAllEmployeesDetails(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    private ResponseEntity<Employee> getEmployeeDetailsById(
            @NotNull @PathVariable Long id
    ) {
        return employeeService.getEmployeesDetailsById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    private ResponseEntity<Employee> updateEmployeeDetails(
            @PathVariable Long id,
            @Valid
            @RequestBody Employee employee
    ) {
        try {
            Employee updatedEmployee = employeeService.updateEmployeeDetails(id, employee);
            return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Employee> deleteEmployeeById(
            @NotNull @PathVariable Long id
    ) {
        try {
            employeeService.deleteEmployeeById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    private ResponseEntity<Employee> deactivateEmployee(
            @PathVariable Long id
    ){
        try {
            Employee deactivateEmployee = employeeService.deactivateEmployee(id);
            //return new ResponseEntity<>(deactivateEmployee, HttpStatus.OK);
            return ResponseEntity.ok(deactivateEmployee);
        }
        catch(RuntimeException ex){
            return ResponseEntity.notFound().build();
        }
    }
}
