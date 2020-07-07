package com.team.tracking_management_system_backend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageVO {
    MessageVO(String message, State state){
        this.message = message;
    }
    public  final static String STATE = "state";
    private String message;
    public enum State{SUCCESS,FAIL};

}
