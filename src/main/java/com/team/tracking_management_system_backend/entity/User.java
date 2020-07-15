package com.team.tracking_management_system_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class User {
    public User() {
    }
    public User(User.Role role,String name, int number){
        this.role =role;
        this.name = name;
        this.number = number;
    }

    public enum Role{
        ADMIN,MANAGER,EMPLOYEE
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @Column(unique = true)
    private Integer number;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    //只能反序列化为java，不能序列化为json。也就是json返回不到前端
    private String password;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Role role;
    @JsonIgnore
    @Column(columnDefinition = "timestamp default current_timestamp",
            insertable = false,
            updatable = false)
    private LocalDateTime insertTime;
    @Column(columnDefinition = "timestamp default current_timestamp",
            insertable = false,
            updatable = false)
    @JsonIgnore
    private LocalDateTime updateTime;
}
