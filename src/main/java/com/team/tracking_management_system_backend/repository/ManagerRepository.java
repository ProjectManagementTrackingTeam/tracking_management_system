package com.team.tracking_management_system_backend.repository;


import com.team.tracking_management_system_backend.entity.Manager;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends BaseRepository<Manager,Integer> {
//    Manager findByNumber(int number);
}
