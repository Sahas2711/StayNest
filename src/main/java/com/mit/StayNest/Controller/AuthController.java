package com.mit.StayNest.Controller;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mit.StayNest.Entity.JwtRequest;
import com.mit.StayNest.Entity.JwtResponse;
import com.mit.StayNest.Entity.User;
import com.mit.StayNest.Repository.UserRepository;
import com.mit.StayNest.Security.JwtHelper;

@RestController
public class AuthController {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthenticationManager manager;

	@Autowired
	private JwtHelper helper;

	@Autowired
	private UserRepository userRepo;
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@PostMapping("/login")
	public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
		logger.info("Login attempt for user: {}", request.getEmail());

		this.doAuthenticate(request.getEmail(), request.getPassword());

		UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
		String token = this.helper.generateToken(userDetails);

		User user = userRepo.findByEmail(request.getEmail())
				.orElseThrow(() -> new RuntimeException("User not found with email: " + request.getEmail()));

		JwtResponse response = new JwtResponse(token, user.getEmail(), user.getId());

		logger.info("JWT Token generated for user: {}", user.getEmail());

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	private void doAuthenticate(String email, String password) {
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
		try {
			manager.authenticate(authentication);
		} catch (BadCredentialsException e) {
			logger.warn("Authentication failed for user: {}", email);
			throw new BadCredentialsException("Invalid Username or Password !!");
		}
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<String> exceptionHandler(BadCredentialsException ex) {
		return new ResponseEntity<>("Credentials Invalid !!", HttpStatus.UNAUTHORIZED);
	}
}
