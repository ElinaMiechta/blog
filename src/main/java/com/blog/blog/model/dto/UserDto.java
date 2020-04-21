package com.blog.blog.model.dto;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UserDto {

    @NotBlank(message = "Type your username")
    private String username;

    @NotBlank(message = "Your email is invalid")
    @Pattern(regexp = "^[a-z0-9](\\.?[a-z0-9]){5,}@g(oogle)?mail\\.com$")
    private String email;

    @NotBlank
    @Size(min = 5, message = "Password too short")
    @Pattern(regexp = "([A-Z]+.*[0-9]+)|([0-9]+.*[A-Z])", message = "Your password must have one capital letter and one digit")
    private String password;

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
