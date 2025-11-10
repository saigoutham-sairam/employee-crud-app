package com.example.employee.controller;

import com.example.employee.model.Employee;
import com.example.employee.service.EmployeeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/")
    private ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee employee) {
        Employee createdEmployee = employeeService.createEmployee(employee);
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }

    @GetMapping()
    private ResponseEntity<Employee> getEmployeeDetails() {
        employeeService.getAllEmployeesDetails();
        return null;
    }

    @GetMapping("/{id}")
    private ResponseEntity<List<Employee>> getAllEmployeeDetails(
            @NotNull @PathVariable Long id
    ) {
        employeeService.getEmployeesDetailsById(id);
        return null;
    }

    @PutMapping("/{id}")
    private ResponseEntity<Employee> updateEmployeeDetails(
            @Valid
            @RequestBody Employee employee
    ) {
        employeeService.updateEmployeeDetails(employee);
        return new ResponseEntity<>(new Employee(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Employee> deleteEmployeeById(
            @NotNull @PathVariable Long id
    ) {
        employeeService.deleteEmployeeById(id);
        return new ResponseEntity<>(new Employee(), HttpStatus.OK);
    }
}
