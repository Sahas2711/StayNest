package com.mit.StayNest.Services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mit.StayNest.Entity.User;
import com.mit.StayNest.Repository.UserRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MailService mailService;
    
    @Autowired
    private JavaMailSender javaMailSender;
    
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public List<User> getUser() {
        logger.info("Fetching all users");
        return userRepo.findAll();
    }

    @Override // If this method is overriding an interface method, otherwise remove @Override
    public User register(User user) {
        logger.info("Attempting to register user with email: {}", user.getEmail());

        Optional<User> existingUser = userRepo.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            logger.warn("Registration failed. User already exists with email: {}", user.getEmail());
            throw new RuntimeException("User already exists with email: " + user.getEmail());
        }

        // Encode and save user
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        User savedUser = userRepo.save(user);

        // Prepare dynamic HTML welcome email
        String userName = user.getName() != null ? user.getName() : "There";
        String email = user.getEmail();  // <-- Fix: you missed declaring this!
        String browsePgLink = "http://localhost:3000/login";
        String contactFaqLink = "http://localhost:3000/contact-us";
        String subject = "Welcome to StayNest! Your Nest Away From Home Awaits!";

        String htmlBody = "<!DOCTYPE html>"
                + "<html lang=\"en\">"
                + "<head>"
                + "    <meta charset=\"UTF-8\">"
                + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                + "    <title>Welcome to StayNest!</title>"
                + "    <style>"
                + "        body {"
                + "            font-family: Arial, sans-serif;"
                + "            background-color: #f8f8f8;"
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
                + "            background: linear-gradient(to right, #FFC06A, #FFA36A);"
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
                + "            color: #333333;"
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
                + "            background-color: #FF8C42;"
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
                + "            background-color: #e67e32;"
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
                + "            color: #FF8C42;"
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
                + "                            <p>Welcome to StayNest! We're thrilled to have you join our community.</p>"
                + "                            <p>StayNest helps you find your perfect nest away from home. Explore verified PGs and hostels, book easily, and live comfortably.</p>"
                + "                            <div class=\"button-container\">"
                + "                                <a href=\"" + browsePgLink + "\" class=\"button\" target=\"_blank\">Start Exploring Now</a>" // Dynamic link
                + "                            </div>"
                + "                            <p>If you have any questions or need assistance, feel full to visit our <a href=\"" + contactFaqLink + "\" target=\"_blank\">Help Center</a> or contact our support team.</p>" // Dynamic link
                + "                            <p>Happy nesting!<br>The StayNest Team</p>"
                + "                        </td>"
                + "                    </tr>"
                + "                    <tr>"
                + "                        <td class=\"footer\">"
                + "                            <p>&copy; 2025 StayNest. All rights reserved.</p>"
                + "                            <p><a href=\"#\" target=\"_blank\">Privacy Policy</a> | <a href=\"#\" target=\"_blank\">Terms of Service</a></p>" // Update these if you have actual URLs
                + "                        </td>"
                + "                    </tr>"
                + "                </table>"
                + "            </td>"
                + "        </tr>"
                + "    </table>"
                + "</body>"
                + "</html>";

        // Send HTML Email
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            // Optional: helper.setFrom("noreply@staynest.com");

            javaMailSender.send(message);
            logger.info("Welcome email sent to: {}", email);

        } catch (MessagingException e) {
            logger.error("Failed to send welcome email to {}: {}", email, e.getMessage());
            // Do not fail the registration if email fails
        }

        logger.info("User registered successfully with ID: {}", savedUser.getId());
        return savedUser;
    }

    @Override
    public User login(User user) {
        logger.info("Login attempt for email: {}", user.getEmail());

        if (user.getEmail() == null || user.getPassword() == null) {
            logger.warn("Login failed: missing credentials");
            throw new RuntimeException("Credentials not found");
        }

        Optional<User> existingUser = userRepo.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            if (passwordEncoder.matches(user.getPassword(), existingUser.get().getPassword())) {
                logger.info("Login successful for email: {}", user.getEmail());
                return existingUser.get();
            } else {
                logger.warn("Login failed: invalid password for email: {}", user.getEmail());
            }
        } else {
            logger.warn("Login failed: user not found for email: {}", user.getEmail());
        }

        throw new RuntimeException("Invalid email or password");
    }

    @Override
    public User currentUser(User user) {
        logger.info("Fetching current user by email: {}", user.getEmail());
        Optional<User> currentUser = userRepo.findByEmail(user.getEmail());

        if (currentUser.isPresent()) {
            return currentUser.get();
        } else {
            logger.warn("Current user not found for email: {}", user.getEmail());
            throw new RuntimeException("User does not exist");
        }
    }

    @Override
    public User updateUser(User user) {
        logger.info("Updating user with email: {}", user.getEmail());
        Optional<User> currentUser = userRepo.findByEmail(user.getEmail());

        if (currentUser.isPresent()) {
            User existingUser = currentUser.get();

            if (user.getName() != null) existingUser.setName(user.getName());
            if (user.getPassword() != null) {
                String encodedPassword = passwordEncoder.encode(user.getPassword());
                existingUser.setPassword(encodedPassword);
            }

            if (user.getPhoneNumber() != null) existingUser.setPhoneNumber(user.getPhoneNumber());
            if (user.getRole() != null) existingUser.setRole(user.getRole());

            User updated = userRepo.save(existingUser);
            logger.info("User updated successfully for ID: {}", updated.getId());
            return updated;
        } else {
            logger.warn("Update failed. User not found for email: {}", user.getEmail());
            throw new RuntimeException("User does not exist");
        }
    }

    @Override
    public User deleteUser(User user) {
        logger.info("Attempting to delete user with ID: {}", user.getId());
        Optional<User> currentUser = userRepo.findById(user.getId());

        if (currentUser.isPresent()) {
            userRepo.deleteById(user.getId());
            logger.info("User deleted successfully with ID: {}", user.getId());
            return currentUser.get();
        } else {
            logger.warn("Delete failed. User not found with ID: {}", user.getId());
            throw new RuntimeException("No User exists with this id");
        }
    }

    @Override
    public User getUserById(String id) {
        logger.info("Fetching user by ID: {}", id);
        Optional<User> user = userRepo.findById(Long.parseLong(id));

        if (user.isPresent()) {
            return user.get();
        } else {
            logger.warn("User not found with ID: {}", id);
            throw new RuntimeException("No User exists with this id");
        }
    }

    @Override
    public List<User> getUserByRole(String role) {
        logger.info("Fetching users by role: {}", role);
        List<User> users = userRepo.findByRole(role);
        logger.info("Found {} users with role '{}'", users.size(), role);
        return users;
    }
}
