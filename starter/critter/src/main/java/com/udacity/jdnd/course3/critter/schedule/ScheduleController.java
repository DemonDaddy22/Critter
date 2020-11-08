package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import com.udacity.jdnd.course3.critter.user.Employee;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PetService petService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = convertScheduleDTOToSchedule(scheduleDTO);
        return convertScheduleToScheduleDTO(scheduleService.save(schedule));
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> schedules = scheduleService.getAllSchedules();
        List<ScheduleDTO> scheduleDTOs = new ArrayList<>();

        schedules.forEach(schedule -> scheduleDTOs.add(convertScheduleToScheduleDTO(schedule)));

        return scheduleDTOs;
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        List<Schedule> schedules = scheduleService.getSchedulesByPet(petService.findPet(petId));
        List<ScheduleDTO> scheduleDTOs = new ArrayList<>();

        schedules.forEach(schedule -> scheduleDTOs.add(convertScheduleToScheduleDTO(schedule)));

        return scheduleDTOs;
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        List<Schedule> schedules = scheduleService.getSchedulesByEmployee(employeeService.findEmployee(employeeId));
        List<ScheduleDTO> scheduleDTOs = new ArrayList<>();

        schedules.forEach(schedule -> scheduleDTOs.add(convertScheduleToScheduleDTO(schedule)));

        return scheduleDTOs;
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        List<Pet> pets = customerService.findCustomerById(customerId).getPets();
        List<Schedule> schedules = new ArrayList<>();
        List<ScheduleDTO> scheduleDTOs = new ArrayList<>();

        pets.forEach(pet -> schedules.addAll(scheduleService.getSchedulesByPet(pet)));

        schedules.forEach(schedule -> scheduleDTOs.add(convertScheduleToScheduleDTO(schedule)));

        return scheduleDTOs;
    }

    private Schedule convertScheduleDTOToSchedule(ScheduleDTO scheduleDTO){
        Schedule schedule = new Schedule();

        BeanUtils.copyProperties(scheduleDTO, schedule);

        List<Long> petIds = scheduleDTO.getPetIds();
        if(petIds != null){
            List<Pet> pets = new ArrayList<Pet>();
            petIds.forEach(petId -> pets.add(petService.findPet(petId)));
            schedule.setPets(pets);
        }

        List<Long> employeeIds = scheduleDTO.getEmployeeIds();
        if(employeeIds != null){
            List<Employee> employees = new ArrayList<Employee>();
            employeeIds.forEach(employeeId -> employees.add(employeeService.findEmployee(employeeId)));
            schedule.setEmployees(employees);
        }

        return schedule;
    }

    private ScheduleDTO convertScheduleToScheduleDTO(Schedule schedule){
        ScheduleDTO scheduleDTO = new ScheduleDTO();

        BeanUtils.copyProperties(schedule, scheduleDTO);

        List<Pet> pets = schedule.getPets();
        if(pets != null){
            List<Long> petIds = new ArrayList<Long>();
            pets.forEach(pet -> petIds.add(pet.getId()));
            scheduleDTO.setPetIds(petIds);
        }

        List<Employee> employees = schedule.getEmployees();
        if(employees != null){
            List<Long> employeeIds = new ArrayList<Long>();
            employees.forEach(employee -> employeeIds.add(employee.getId()));
            scheduleDTO.setEmployeeIds(employeeIds);
        }

        return scheduleDTO;
    }
}
