package com.blog.blog.controller;

import com.blog.blog.model.User;
import com.blog.blog.model.dto.LoginDto;
import com.blog.blog.model.dto.UserDto;
import com.blog.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.mail.MessagingException;

import javax.servlet.http.HttpServletRequest;

import javax.validation.Valid;
import java.io.FileNotFoundException;


@Controller
public class UserController {
    @Autowired
    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public String registerNewUser(@ModelAttribute("user") @Valid UserDto userDto,
                                  BindingResult bindingResult) throws MessagingException, FileNotFoundException {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        userService.saveUserToDB(userDto);
        return "welcome";

    }

    @GetMapping("/register")
    public String register(Model model) {


        model.addAttribute("user", new UserDto());
        return "register";
    }

    @GetMapping
    public String mainPage(@Valid User user, Model model, HttpServletRequest request) {
        model.addAttribute("user", user);

        return "welcome";
    }




    @GetMapping("/login")
    public String loginUser(@ModelAttribute("user") @Valid User user) {

        return "login";
    }


    @PostMapping("/login")
    public String loginUser(@ModelAttribute("user") @Valid LoginDto loginDto,
                            BindingResult bindingResult, HttpServletRequest request, Model model) {
        if (bindingResult.hasErrors()) {
            return "login";
        }

        User userByEmailAndPassword = userService.findUserByEmailAndPassword(loginDto.getEmail(), loginDto.getPassword());
        if (userByEmailAndPassword.getRole().equals("ROLE_ADMIN")){

            return "admin";
        }

        return "welcome";
    }





}
