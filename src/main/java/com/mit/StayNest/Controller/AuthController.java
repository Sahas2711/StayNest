package com.mit.StayNest.Controller;

import com.mit.StayNest.Entity.JwtRequest;
import com.mit.StayNest.Entity.JwtResponse;
import com.mit.StayNest.Entity.Owner;
import com.mit.StayNest.Entity.User;
import com.mit.StayNest.Payloads.LoginRequest;
import com.mit.StayNest.Repository.OwnerRepository;
import com.mit.StayNest.Repository.UserRepository;
import com.mit.StayNest.Security.JwtHelper;
import com.mit.StayNest.Services.CustomUserDetailsService;
import com.mit.StayNest.Services.MailService;
//import com.mit.StayNest.Services.OwnerDetailsService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.naming.AuthenticationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@RestController
public class AuthController {

	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
//	@Autowired
//	private OwnerDetailsService ownerDetailsService;


    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private JwtHelper helper;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private OwnerRepository ownerRepo;
    
    @Autowired
    private MailService mailService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    

    @Autowired 
    private JavaMailSender javaMailSender;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    @PostMapping("/login/user")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {
        logger.info("Login attempt as USER for email: {}", request.getEmail());

        try {
            // 1. Authenticate with AuthenticationManager
            Authentication authentication = manager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            // 2. Load UserDetails from UserDetailsService
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.getEmail());

            // 3. Generate JWT Token
            String token = helper.generateToken(userDetails);

            // 4. Fetch User entity for additional info
            User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found after auth."));

            JwtResponse response = new JwtResponse(token, user.getName(), user.getId());
            logger.info("Login successful as USER: {}", request.getEmail());
            return ResponseEntity.ok(response);

        } catch (BadCredentialsException ex) {
            logger.warn("Login failed for USER: Invalid credentials for {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid email or password (USER)"));
        } catch (Exception e) {
            logger.error("Unexpected error during USER login", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Something went wrong. Please try again."));
        }
    }


//    @PostMapping("/login/user")
//    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {
//        logger.info("Login attempt as USER for email: {}", request.getEmail());
//
//        // Check user existence
//        User user = userRepo.findByEmail(request.getEmail()).orElse(null);
//        if (user == null) {
//            logger.warn("Login failed: USER not found for {}", request.getEmail());
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
//        }
//
//        // Check password manually to avoid infinite loop
//        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            logger.warn("Login failed: Incorrect USER password for {}", request.getEmail());
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
//        }
//
//        // Authentication success - generate JWT
//        String token = helper.generateToken(new org.springframework.security.core.userdetails.User(
//                user.getEmail(), user.getPassword(), new ArrayList<>()
//        ));
//
//        JwtResponse response = new JwtResponse(token, user.getName(), user.getId());
//        logger.info("Login successful as USER: {}", request.getEmail());
//        return ResponseEntity.ok(response);
//    }
    @PostMapping("/login/owner")
    public ResponseEntity<?> loginOwner(@RequestBody LoginRequest request) {
        logger.info("Login attempt as OWNER for email: {}", request.getEmail());

        try {
            // 1. Authenticate with AuthenticationManager
            Authentication authentication = manager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            // 2. Load details from OwnerDetailsService (ensure this loads Owner entity)
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.getEmail());

            // 3. Generate JWT Token
            String token = helper.generateToken(userDetails);

            // 4. Fetch additional info from Owner entity
            Owner owner = ownerRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Owner not found after auth."));

            JwtResponse response = new JwtResponse(token, owner.getName(), owner.getId());
            logger.info("Login successful as OWNER: {}", request.getEmail());
            return ResponseEntity.ok(response);

        } catch (BadCredentialsException ex) {
            logger.warn("Login failed for OWNER: Invalid credentials for {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid email or password (OWNER)"));
        } catch (Exception e) {
            logger.error("Unexpected error during OWNER login", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Something went wrong. Please try again."));
        }
    }


//    @PostMapping("/login/owner")
//    public ResponseEntity<?> loginOwner(@RequestBody LoginRequest request) {
//        logger.info("Login attempt as OWNER for email: {}", request.getEmail());
//
//        // Check owner existence
//        Owner owner = ownerRepo.findByEmail(request.getEmail()).orElse(null);
//        if (owner == null) {
//            logger.warn("Login failed: OWNER not found for {}", request.getEmail());
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
//        }
//
//        // Check password manually
//        if (!passwordEncoder.matches(request.getPassword(), owner.getPassword())) {
//            logger.warn("Login failed: Incorrect OWNER password for {}", request.getEmail());
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
//        }
//
//        // Authentication success - generate JWT
//        String token = helper.generateToken(new org.springframework.security.core.userdetails.User(
//                owner.getEmail(), owner.getPassword(), new ArrayList<>()
//        ));
//
//        JwtResponse response = new JwtResponse(token, owner.getName(), owner.getId());
//        logger.info("Login successful as OWNER: {}", request.getEmail());
//        return ResponseEntity.ok(response);
//    }


    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        Optional<User> userOpt = userRepo.findByEmail(email);
        Optional<Owner> ownerOpt = ownerRepo.findByEmail(email);
        String userName;
        if (userOpt.isPresent()) {
             userName = userOpt.get().getName(); // Get the user's name
        } else if(ownerOpt.isPresent()){
        	 userName = ownerOpt.get().getName();
        }else {
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with this email does not exist.");
        }
     // Construct the reset link. Make sure this matches your frontend's reset password route.
        String resetLink = "http://localhost:3000/reset-password?email=" + email; // Use your actual domain in production

        String subject = "Reset Your Password - StayNest";

        // The detailed HTML email template
        String htmlBody = "<!DOCTYPE html>"
                + "<html lang=\"en\">"
                + "<head>"
                + "    <meta charset=\"UTF-8\">"
                + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                + "    <title>Reset Your Password - StayNest</title>"
                + "    <style>"
                + "        body {"
                + "            font-family: Arial, sans-serif;"
                + "            background-color: #f8f8f8; /* Light background similar to your site */"
                + "            margin: 0;"
                + "            padding: 0;"
                + "            -webkit-text-size-adjust: none;"
                + "            width: 100% !important;"
                + "        }"
                + "        table {"
                + "            border-collapse: collapse;"
                + "            width: 100%;"
                + "        }"
                + "        td {"
                + "            padding: 0;"
                + "        }"
                + "        .container {"
                + "            max-width: 600px;"
                + "            margin: 20px auto;"
                + "            background-color: #ffffff;"
                + "            border-radius: 8px;"
                + "            overflow: hidden;"
                + "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);"
                + "        }"
                + "        .header {"
                + "            background: linear-gradient(to right, #FFC06A, #FFA36A); /* Orange gradient from your site */"
                + "            padding: 20px;"
                + "            text-align: center;"
                + "        }"
                + "        .header h1 {"
                + "            color: #ffffff;"
                + "            font-size: 28px;"
                + "            margin: 0;"
                + "            font-weight: bold;"
                + "        }"
                + "        .content {"
                + "            padding: 30px;"
                + "            color: #333333; /* Dark text color */"
                + "            line-height: 1.6;"
                + "        }"
                + "        .content p {"
                + "            margin-bottom: 15px;"
                + "            font-size: 16px;"
                + "        }"
                + "        .button-container {"
                + "            text-align: center;"
                + "            padding: 20px 0;"
                + "        }"
                + "        .button {"
                + "            display: inline-block;"
                + "            background-color: #FF8C42; /* Primary orange button color */"
                + "            color: #ffffff !important;"
                + "            padding: 12px 25px;"
                + "            text-decoration: none;"
                + "            border-radius: 5px;"
                + "            font-weight: bold;"
                + "            font-size: 16px;"
                + "            box-shadow: 0 4px 8px rgba(255, 140, 66, 0.3);"
                + "            transition: background-color 0.3s ease;"
                + "        }"
                + "        .button:hover {"
                + "            background-color: #e67e32; /* Slightly darker orange on hover */"
                + "        }"
                + "        .footer {"
                + "            background-color: #f2f2f2;"
                + "            padding: 20px;"
                + "            text-align: center;"
                + "            font-size: 12px;"
                + "            color: #777777;"
                + "        }"
                + "        .footer p {"
                + "            margin: 0;"
                + "        }"
                + "        a {"
                + "            color: #FF8C42; /* Link color */"
                + "            text-decoration: none;"
                + "        }"
                + "        a:hover {"
                + "            text-decoration: underline;"
                + "        }"
                + "    </style>"
                + "</head>"
                + "<body>"
                + "    <table role=\"presentation\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">"
                + "        <tr>"
                + "            <td align=\"center\">"
                + "                <table role=\"presentation\" class=\"container\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">"
                + "                    <tr>"
                + "                        <td class=\"header\">"
                + "                            <h1>StayNest</h1>"
                + "                        </td>"
                + "                    </tr>"
                + "                    <tr>"
                + "                        <td class=\"content\">"
                + "                            <p>Hello " + userName + ",</p>" // Dynamic user name
                + "                            <p>We received a request to reset your password for your StayNest account. If you did not make this request, you can safely ignore this email.</p>"
                + "                            <p>To reset your password, please click on the button below:</p>"
                + "                            <div class=\"button-container\">"
                + "                                <a href=\"" + resetLink + "\" class=\"button\" target=\"_blank\">Reset Your Password</a>" // Dynamic reset link
                + "                            </div>"
                + "                            <p>This link is valid for a limited time (e.g., 24 hours) for security reasons. If the button above doesn't work, you can copy and paste the following link into your web browser:</p>"
                + "                            <p style=\"word-break: break-all;\"><a href=\"" + resetLink + "\" target=\"_blank\">" + resetLink + "</a></p>" // Dynamic reset link
                + "                            <p>If you have any questions or need further assistance, please don't hesitate to contact our support team.</p>"
                + "                            <p>Thank you,<br>The StayNest Team</p>"
                + "                        </td>"
                + "                    </tr>"
                + "                    <tr>"
                + "                        <td class=\"footer\">"
                + "                            <p>&copy; 2025 StayNest. All rights reserved.</p>"
                + "                            <p><a href=\"#\" target=\"_blank\">Privacy Policy</a> | <a href=\"#\" target=\"_blank\">Terms of Service</a></p>"
                + "                        </td>"
                + "                    </tr>"
                + "                </table>"
                + "            </td>"
                + "        </tr>"
                + "    </table>"
                + "</body>"
                + "</html>";

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); 
            helper.setFrom("no-reply@staynest.com");

            javaMailSender.send(message);

            return ResponseEntity.ok("Password reset email sent.");
        } catch (MessagingException e) {
            // Log the exception for debugging purposes
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email. " + e.getMessage());
        }
    }
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String email, @RequestParam String newPassword) {
    	logger.info("Reset password request initiated for {}",email);
        Optional<User> userOpt = userRepo.findByEmail(email);
        Optional<Owner> ownerOpt = ownerRepo.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            logger.info("Password reset successful for {}",email);
            userRepo.save(user);
            return ResponseEntity.ok("Password updated successfully.");
        }else if(ownerOpt.isPresent()) {
        	Owner owner = ownerOpt.get();
        	owner.setPassword(passwordEncoder.encode(newPassword));
            ownerRepo.save(owner);
            logger.info("Password reset successful for {}",email);
            return ResponseEntity.ok("Password updated successfully.");
        }
        else {
        	logger.warn("User or owner dosen't exist with mail");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
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