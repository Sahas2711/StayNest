package com.mit.StayNest.Services;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mit.StayNest.Entity.User;
import com.mit.StayNest.Repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public List<User> getUser() {
        return userRepo.findAll();
    }

    @Override
    public User register(User user) {
    	if(user.getEmail() == null || user.getPassword() == null) throw new RuntimeException("Credentials not found");
        
        Optional<User> existingUser = userRepo.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("User already exists with email: " + user.getEmail());
        }
   
        return userRepo.save(user);
    }

    @Override
    public User login(User user) {
    	if(user.getEmail() == null || user.getPassword() == null) throw new RuntimeException("Credentials not found");
        Optional<User> existingUser = userRepo.findByEmail(user.getEmail());

        if (existingUser.isPresent() && existingUser.get().getPassword().equals(user.getPassword())) {
            return existingUser.get(); 
        }

        throw new RuntimeException("Invalid email or password");
    }
}
