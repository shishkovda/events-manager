package com.solution.eventsmanager.controller;

import com.solution.eventsmanager.entity.User;
import com.solution.eventsmanager.representation.UserRepresentation;
import com.solution.eventsmanager.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    Logger logger = LoggerFactory.getLogger(TemplateController.class);

    @Autowired
    UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("")
    public void createUser(@RequestBody UserRepresentation userRepresentation) {
        logger.info("createUser(): userRepresentation = " + userRepresentation.toString());

        User user = new User();
        user.setCardNumber(userRepresentation.getCardNumber());
        user.setEmail(userRepresentation.getEmail());
        user.setFirstName(userRepresentation.getFirstName());
        user.setLastName(userRepresentation.getLastName());
        user.setLogin(userRepresentation.getLogin());
        user.setPassword(passwordEncoder.encode(userRepresentation.getPassword()));
        user.setPhoneNumber(userRepresentation.getPhoneNumber());
        userService.createUser(user);
    }

    @GetMapping
    public ResponseEntity<User> getUser(@RequestParam String phoneNumber){
        logger.info("getUser(): phoneNumber = " + phoneNumber);

        User user = userService.getUserByPhoneNUmber(phoneNumber);
        logger.info("getUser(): user = " + user.toString());

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") String id){
        return new ResponseEntity<>(userService.getUSerById(Long.valueOf(id)), HttpStatus.OK);
    }

}
