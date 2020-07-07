package com.team.tracking_management_system_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    //紧急程度
    private int emergencyDegree;
    //开始时间
    private LocalDateTime startTime;
    //结束时间
    private LocalDateTime endTime;
    //与管理员是多对一关系
    @ManyToOne
    private Manager manager;
    @OneToMany(mappedBy = "project")
    private List<ProjectFile> projectFiles;
    //与员工是一对多关系
    @OneToMany(mappedBy = "project")
    private List<Employee> employees;
    @Column(columnDefinition = "timestamp default current_timestamp",
            insertable = false,
            updatable = false)
    private LocalDateTime insertTime;
    @Column(columnDefinition = "timestamp default current_timestamp",
            insertable = false,
            updatable = false)
    private LocalDateTime updateTime;


}
