package com.mit.StayNest.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.resource.HttpResource;

import com.mit.StayNest.Entity.User;
import com.mit.StayNest.Services.UserService;

@RestController
public class UserController {

	@Autowired
	UserService userService ;
	
    @PostMapping("/register")
    public User register(@RequestBody User request) {
        // registration logic here
        return this.userService.register(request);
    }

    @PostMapping("/login")
    public User login(@RequestBody User request) {
        // login logic here
        return this.userService.login(request);
    }
}

