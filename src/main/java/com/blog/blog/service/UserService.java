package com.blog.blog.service;

import com.blog.blog.configuration.EmailService;
import com.blog.blog.model.User;
import com.blog.blog.model.dto.UserDto;
import com.blog.blog.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;


@Service
public class UserService {

    private final UserRepository userRepository;
    @Autowired
    private EmailService emailService;

    @Autowired
    private JavaMailSender mailSender;


    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUserToDB(UserDto userDto) {
        String role = "ROLE_USER";

        int tokenLength = 50;
        String token = RandomStringUtils.randomAlphanumeric(tokenLength);
        String encodedPassword = new BCryptPasswordEncoder().encode(userDto.getPassword());
        User user = new User(userDto.getUsername(), encodedPassword,
                userDto.getEmail(), role, token);

        userRepository.save(user);
        confirmRegistrationByEmail(user);
        return user;


    }


    private void confirmRegistrationByEmail(@Valid User user) {


        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String token = user.getToken();


        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        String body = user.getUsername() + ", welcome to blog about programming, OOP and more!\r\n" + "\r\n"
                + "Browse your favorite books, our editorial picks, bestsellers, or customer favorites.\r\n" + "\r\n";

        email.setText(body + "\r\n" + "http://localhost:8080" + "/" + token);


//        emailService.sendSimpleMessage(to, from, subject, body);
        //emailService.sendMessageWithAttachment(recipientAddress, subject, body, "/Users/elann/Documents/blog-springboot/src/main/resources/static/img/home-bg.jpg");
        mailSender.send(email);
    }


    public User findUserByEmailAndPassword(String email, String password) {
        return userRepository.findUserByEmailAndPassword(email, password);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findUserByToken(String token) {
        return userRepository.findUserByToken(token);
    }

    @Transactional
    public void activateNewUser(String token) {
        userRepository.activateUser(token);
    }

    @Transactional
    public void updateLoginDate(String email) {
        userRepository.updateLoginDate(email);
    }

    @Transactional
    public void deactivateUser(LocalDate date) {
        userRepository.deactivateUser(date);
    }

    @Transactional
    public void activateReturnedUser(String token, String email) {
        userRepository.saveNewToken(token,email);
        User user = findUserByToken(token);
        confirmRegistrationByEmail(user);


    }



}
