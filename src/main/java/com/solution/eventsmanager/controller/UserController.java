package com.solution.eventsmanager.controller;

import com.solution.eventsmanager.entity.User;
import com.solution.eventsmanager.representation.UserRepresentation;
import com.solution.eventsmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Attribute;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("")
    public void createUser(@RequestBody UserRepresentation userRepresentation) {
        User user = new User();
        user.setCardNumber(userRepresentation.getCardNumber());
        user.setEmail(userRepresentation.getEmail());
        user.setFirstName(userRepresentation.getFirstName());
        user.setLastName(userRepresentation.getLastName());
        user.setLogin(userRepresentation.getLogin());
        user.setPassword(userRepresentation.getPassword());
        user.setPhoneNumber(userRepresentation.getPhoneNumber());
        userService.createUser(user);
    }

    @GetMapping
    public ResponseEntity<User>                                                                                                                     getUser(@RequestParam String phoneNumber){
        return new ResponseEntity<>(userService.getUserByPhoneNUmber(phoneNumber), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") String id){
        return new ResponseEntity<>(userService.getUSerById(Long.valueOf(id)), HttpStatus.OK);
    }

}
