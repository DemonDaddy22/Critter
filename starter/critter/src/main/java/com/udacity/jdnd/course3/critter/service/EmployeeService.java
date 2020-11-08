package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee findEmployee(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public List<Employee> findEmployeesAvailable(LocalDate date, Set<EmployeeSkill> employeeSkills) {
        List<Employee> employees = employeeRepository.getEmployeesByDaysAvailable(date.getDayOfWeek());

        List<Employee> foundEmployees = new ArrayList<>();

        for (Employee employee : employees){
            if(employee.getSkills().containsAll(employeeSkills)){
                foundEmployees.add(employee);
            }
        }

        return foundEmployees;
    }

    public void setDaysAvailable(Set<DayOfWeek> availableDays, Long employeeId){
        Employee employee = employeeRepository.findById(employeeId).orElse(null);

        if (employee == null){
            return;
        }

        employee.setDaysAvailable(availableDays);
        employeeRepository.save(employee);
    }
}
