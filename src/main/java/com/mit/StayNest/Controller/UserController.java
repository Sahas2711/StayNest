
package com.mit.StayNest.Controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    
    @GetMapping("/getusers")
    public List<User> getUsers(){
    	return this.userService.getUser();
    }
    
    @GetMapping("/users/me")
    public User currentUser(@RequestBody User user) {
    	return this.userService.currentUser(user);
    }
    
    @PutMapping("/users/update")
    public User updateUser(@RequestBody User user) {
    	return this.userService.updateUser(user);
    }
    
    @DeleteMapping("/users/delete")
    public User deleteUSer(@RequestBody User user) {
    	return this.userService.deleteUser(user);
    }
    
    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable String id) {
    	return this.userService.getUserById(id);
    }
    
    @GetMapping("/users/role")
    public List<User> getUserByrole(@RequestParam String role){
    	return this.userService.getUserByRole(role);
    }
    
    
    
}

