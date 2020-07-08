package com.team.tracking_management_system_backend.repository;

import com.team.tracking_management_system_backend.entity.Admin;
import com.team.tracking_management_system_backend.entity.Project;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends BaseRepository<Project,Integer>{
    @Query("from Project p where p.name =:name and p.manager.id=:managerId")
    Optional<Project> findProjectByName(int managerId,String name);
    @Query("from Project p where p.manager.id =: managerId")
    List<Project> getProjects(int managerId);
    @Modifying
    @Query("delete from Project p where p.id in :projectsIdList")
    void deleteProjects(List<Integer> projectsIdList);
    //todo 这里可能会有问题
    @Modifying
    @Query("update  Project p set p.manager.id =:managerId where p.id in :transferIds")
    void transferProjects(List<Integer> transferIds,int managerId);
}
