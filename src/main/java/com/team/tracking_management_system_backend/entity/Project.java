package com.team.tracking_management_system_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@JsonIgnoreProperties({"projectFiles","employees","tasks"})
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    //进度是后端计算的，所以不能反序列化
    //并且也不需要映射到数据库中，在每次查的时候手动赋值就行
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Transient
    private double process;
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
    @OneToMany(mappedBy = "project")
    private List<Task> tasks;
    @Column(columnDefinition = "timestamp default current_timestamp",
            insertable = false,
            updatable = false)
    private LocalDateTime insertTime;
    @Column(columnDefinition = "timestamp default current_timestamp",
            insertable = false,
            updatable = false)
    private LocalDateTime updateTime;


}
