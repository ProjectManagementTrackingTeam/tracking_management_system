package com.team.tracking_management_system_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    @MapsId
    private User user;//依靠单向@OneToOne和@MapsId，与父表共享主键

    @Column(columnDefinition = "timestamp default current_timestamp",
            insertable = false,
            updatable = false)
    //插入时间
    private LocalDateTime insertTime;
    @Column(columnDefinition = "timestamp default current_timestamp",
            insertable = false,
            updatable = false)
    //更新时间
    private LocalDateTime updateTime;


}
