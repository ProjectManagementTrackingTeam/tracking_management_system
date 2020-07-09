package com.team.tracking_management_system_backend.repository;

import com.team.tracking_management_system_backend.entity.Admin;
import com.team.tracking_management_system_backend.entity.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends BaseRepository<Employee,Integer>{
    @Query("from Employee e where e.project.id in (:projectIds)")
    List<Employee>  getEmployee(List<Integer> projectIds);
    @Query("from Employee e where e.project.id is null or e.project.id in (:projectsIds)")
    List<Employee> getAvailable(List<Integer> projectsIds);

}
