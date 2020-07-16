package com.team.tracking_management_system_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.team.tracking_management_system_backend.util.CustomLocalDateTimeDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MyProjectFile {
        @Transient
    public static final String FILEPATH = "C:\\testFile\\";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String fileKey;
    @ManyToOne
    private Project project;
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(columnDefinition = "timestamp default current_timestamp",
            insertable = false,
            updatable = false)
    private LocalDateTime uploadTime;
    @Column(columnDefinition = "timestamp default current_timestamp " +
            "on update current_timestamp",
            insertable = false,
            updatable = false)
    @JsonIgnore
    private LocalDateTime updateTime;
}


