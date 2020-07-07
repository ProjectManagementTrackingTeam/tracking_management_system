package com.team.tracking_management_system_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToMany(mappedBy = "employee")
    private List<TaskEmployee> taskEmployees;
    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    @MapsId
    private User user;//依靠单向@OneToOne和@MapsId，与父表共享主键

    @Column(columnDefinition = "timestamp default current_timestamp",
            insertable = false,
            updatable = false)
    private LocalDateTime insertTime;
    @Column(columnDefinition = "timestamp default current_timestamp",
            insertable = false,
            updatable = false)
    private LocalDateTime updateTime;
    //与project是多对一关系
    @ManyToOne
    private Project project;

}
