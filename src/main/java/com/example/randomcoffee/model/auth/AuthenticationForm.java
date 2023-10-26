package com.example.randomcoffee.model.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class AuthenticationForm {

    /**
     * Required user credential field
     */
    @NotEmpty(message = "Email should not be empty")
    @Email
    private String username;
    /**
     * Required user credential field
     */
    @NotEmpty(message = "Password should not be empty")
    private String password;
}
