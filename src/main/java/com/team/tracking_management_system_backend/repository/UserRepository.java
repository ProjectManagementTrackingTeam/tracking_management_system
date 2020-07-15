package com.team.tracking_management_system_backend.repository;


import com.team.tracking_management_system_backend.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<User, Integer> {
    @Query("from User u where u.number = :number")
    User findByNumber(int number);
    @Query("from User u where u.number =:number and u.name = :name")
    User findByNumberAndName(int number,String name);
}
