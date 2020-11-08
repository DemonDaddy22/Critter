package com.udacity.jdnd.course3.critter.repository;

import com.udacity.jdnd.course3.critter.user.Day;
import com.udacity.jdnd.course3.critter.user.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    public List<Employee> getEmployeesByDaysAvailable(Day day);
}
