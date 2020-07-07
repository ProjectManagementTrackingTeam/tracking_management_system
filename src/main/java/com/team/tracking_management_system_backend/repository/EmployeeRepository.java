package com.team.tracking_management_system_backend.repository;


import com.team.tracking_management_system_backend.entity.Employee;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends BaseRepository<Employee,Integer> {
}
