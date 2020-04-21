package com.blog.blog.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
@Data
public class LoginDto {

    @NotBlank(message = " email is required!")
    @Pattern(regexp = "^[a-z0-9](\\.?[a-z0-9]){5,}@g(oogle)?mail\\.com$")
    private String email;

    @NotBlank(message = "Password can not be empty")
    @Size(min = 5, message = "Password too short")
    @Pattern(regexp = "([A-Z]+.*[0-9]+)|([0-9]+.*[A-Z])", message = "Your password must have one capital letter and one digit")
    private String password;



    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }


}
