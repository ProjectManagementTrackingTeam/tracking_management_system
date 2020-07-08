package com.team.tracking_management_system_backend.repository;

import com.team.tracking_management_system_backend.entity.TaskEmployee;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskEmployeeRepository extends BaseRepository<TaskEmployee,Integer>{
    @Query("from TaskEmployee te where te.task.id =:taskId")
    public List<TaskEmployee> getEmployeeList(int taskId);
    @Modifying
    @Query("delete from TaskEmployee te where te.id in :taskEmpIds")
    public void deleteWorker(List<Integer> taskEmpIds);
}
