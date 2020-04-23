package com.blog.blog.controller;

import com.blog.blog.model.User;
import com.blog.blog.model.dto.LoginDto;
import com.blog.blog.model.dto.UserDto;
import com.blog.blog.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;



@Controller
public class UserController {
    @Autowired
    private final UserService userService;
    @Autowired
    ApplicationEventPublisher eventPublisher;


    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping(value = "/{token}")
    public String activateUserByToken(Model model, @PathVariable String token) {
        User user = userService.findUserByToken(token);
        userService.activateNewUser(token);
        model.addAttribute("user", user);
        return "/confirmationTemplates/regitrationConfirm";
    }

    @GetMapping("/regitrationConfirm")
    public String registrationSuccess() {
        return "/confirmationTemplates/regitrationConfirm";
    }


    @PostMapping("/register")
    public ModelAndView registerUserAccount(
            @ModelAttribute("user") @Valid UserDto userDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ModelAndView("register", "user", userDto);

        }

        userService.saveUserToDB(userDto);

        return new ModelAndView("welcome", "user", userDto);
    }


    @GetMapping("/register")
    public String register(Model model) {


        model.addAttribute("user", new UserDto());
        return "register";
    }

    @GetMapping
    public String mainPage(@Valid User user, Model model) {
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

        userService.findUserByEmailAndPassword(loginDto.getEmail(), loginDto.getPassword());
        userService.updateLoginDate(loginDto.getEmail());
        User user = userService.findByEmail(loginDto.getEmail());

        userService.deactivateUser(user.getLoginDate());


        model.addAttribute("loggedUser", user);

        if (loginDto.getEmail().equals("blogadmin@gmail.com")) {

            return "admin";
        } else

            return "welcome";
    }

    @PostMapping("/activation")
    public String activateOldDeactivatedUser(@Valid LoginDto loginDto){
        int tokenLength = 50;
        String token = RandomStringUtils.randomAlphanumeric(tokenLength);
        userService.activateReturnedUser(token,loginDto.getEmail());


        return "/confirmationTemplates/activationConfirm";
    }

    @GetMapping("/activationConfirm")
    public String activationConfirm(){
        return "/confirmationTemplates/activationConfirm";
    }


}
