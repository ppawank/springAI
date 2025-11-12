package com.app.learningspringai.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Users {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;

}
