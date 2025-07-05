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
import org.springframework.web.bind.annotation.RestController;

import com.mit.StayNest.Entity.Owner;
import com.mit.StayNest.Services.OwnerService;


@RestController
@RequestMapping("/owner")
public class OwnerController {

	@Autowired
	OwnerService ownerService ;
	
    @PostMapping("/register")
    public Owner register(@RequestBody Owner request) {
        // registration logic here
        return this.ownerService.register(request);
    }

    @PostMapping("/login")
    public Owner login(@RequestBody Owner request) {
        // login logic here
        return this.ownerService.login(request);
    }
    
    @GetMapping("/getusers")
    public List<Owner> getUsers(){
    	return this.ownerService.getOwner();
    }
    
    @GetMapping("/users/me")
    public Owner currentUser(@RequestBody Owner user) {
    	return this.ownerService.currentOwner(user);
    }
    
    @PutMapping("/users/update")
    public Owner updateUser(@RequestBody Owner user) {
    	return this.ownerService.updateOwner(user);
    }
    
    @DeleteMapping("/users/delete")
    public Owner deleteUSer(@RequestBody Owner user) {
    	return this.ownerService.deleteOwner(user);
    }
    
    @GetMapping("/users/{id}")
    public Owner getUserById(@PathVariable String id) {
    	return this.ownerService.getOwnerById(id);
    }    
    
}
