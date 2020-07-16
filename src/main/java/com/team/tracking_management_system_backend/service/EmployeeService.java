package com.team.tracking_management_system_backend.service;

import com.team.tracking_management_system_backend.entity.Employee;
import com.team.tracking_management_system_backend.entity.User;
import com.team.tracking_management_system_backend.repository.EmployeeRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Data
@Transactional
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private PasswordEncoder encoder;

    //查询项目经理下的所有员工
    public List<Employee> getEmployeeList(List<Integer> projectIds) {
        return employeeRepository.getEmployee(projectIds);
    }

    //获取项目经理可用的员工
    public List<Employee> getAvailable(List<Integer> projectsIds) {
        return employeeRepository.getAvailable(projectsIds);
    }

    //添加员工，此时员工没有任务，也没有项目，只是为了让他存在
    public void addEmployee(List<Employee> employeeList) {
        List<Employee> collect = employeeList.stream().map(
                (e) -> {
                    User user = e.getUser();
                    user.setPassword(encoder.encode(user.getNumber().toString()));
                    user.setRole(User.Role.EMPLOYEE);
                    return e;
                }
        ).collect(Collectors.toList());
        employeeRepository.saveAll(collect);
    }

    public Employee getEmployeeById(int id) {
        return employeeRepository.getOne(id);

    }
    public Employee getEmplyee(int eid){
        return employeeRepository.findById(eid).orElse(null);
    }



}
