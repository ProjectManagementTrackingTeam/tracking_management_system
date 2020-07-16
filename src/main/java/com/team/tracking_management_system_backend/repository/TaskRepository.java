package com.team.tracking_management_system_backend.repository;

import com.team.tracking_management_system_backend.entity.Task;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends BaseRepository<Task,Integer>{
    @Query("from Task t where t.project.id =:projectId")
    public List<Task> getTasks(int projectId);
    @Modifying
    @Query("delete from Task t where t.id in (?1)")
    public void deleteTasks(List<Integer> tasksIdList);
    @Modifying
    @Query("delete from Task t where t.project.id = :projectId")
    void deleteTasksFromProject(int projectId);
}
