package com.mit.StayNest.Controller;

import com.mit.StayNest.Entity.JwtRequest;
import com.mit.StayNest.Entity.JwtResponse;
import com.mit.StayNest.Entity.Owner;
import com.mit.StayNest.Entity.User;
import com.mit.StayNest.Repository.OwnerRepository;
import com.mit.StayNest.Repository.UserRepository;
import com.mit.StayNest.Security.JwtHelper;
import com.mit.StayNest.Services.CustomUserDetailsService;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

	@Autowired
	
	private UserDetailsService customUserDetailsService;

   
//    @Autowired
//    @Qualifier("ownerUserDetailsService") 
//    private UserDetailsService ownerUserDetailsService;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private JwtHelper helper;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private OwnerRepository ownerRepo;


    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
        logger.info("Login attempt for email: {}", request.getEmail());

        try {
            manager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            logger.info("Authentication successful for email: {}", request.getEmail());
        } catch (BadCredentialsException e) {
            logger.warn("Authentication failed for email: {}", request.getEmail());
            throw e;
        } catch (IllegalArgumentException e) {
            logger.error("Password encoding issue: {}", e.getMessage());
            throw new RuntimeException("Invalid password format. Please reset your password.");
        } 

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.getEmail());
        String token = helper.generateToken(userDetails);
        logger.info("JWT generated for email: {}", request.getEmail());

        Optional<User> user = userRepo.findByEmail(request.getEmail());
        Optional<Owner> owner = ownerRepo.findByEmail(request.getEmail());

        if (user.isPresent()) {
            logger.info("User login successful: ID={}, Email={}", user.get().getId(), user.get().getEmail());
            return ResponseEntity.ok(new JwtResponse(token, user.get().getEmail(), user.get().getId()));
        } else if (owner.isPresent()) {
            logger.info("Owner login successful: ID={}, Email={}", owner.get().getId(), owner.get().getEmail());
            return ResponseEntity.ok(new JwtResponse(token, owner.get().getEmail(), owner.get().getId()));
        } else {
            logger.error("Authenticated email not found in user or owner repository: {}", request.getEmail());
            throw new RuntimeException("User not found after authentication");
        }
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentials(BadCredentialsException ex) {
        logger.warn("BadCredentialsException: {}", ex.getMessage());
        return new ResponseEntity<>("Invalid credentials!", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUsernameNotFound(UsernameNotFoundException ex) {
        logger.warn("UsernameNotFoundException: {}", ex.getMessage());
        return new ResponseEntity<>("User not found!", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointer(NullPointerException ex) {
        logger.error("NullPointerException encountered: ", ex);
        return new ResponseEntity<>("Internal server error: missing data", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntime(RuntimeException ex) {
        logger.error("RuntimeException encountered: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneric(Exception ex) {
        logger.error("Unhandled exception: ", ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}
