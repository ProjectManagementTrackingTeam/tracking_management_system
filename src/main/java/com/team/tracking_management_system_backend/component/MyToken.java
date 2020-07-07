package com.team.tracking_management_system_backend.component;


import com.team.tracking_management_system_backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MyToken {
    public static final String AUTHORIZATION = "Authorization";
    public static final String UID = "uid";
    public static final String ROLE = "role";
    private Integer uid;
    private User.Role role;
}
