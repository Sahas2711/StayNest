
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

		Optional<User> existingUser = userRepo.findByEmail(user.getEmail());
		if (existingUser.isPresent()) {
			throw new RuntimeException("User already exists with email: " + user.getEmail());
		}

		return userRepo.save(user);
	}

	@Override
	public User login(User user) {
		if (user.getEmail() == null || user.getPassword() == null)
			throw new RuntimeException("Credentials not found");
		Optional<User> existingUser = userRepo.findByEmail(user.getEmail());

		if (existingUser.isPresent() && existingUser.get().getPassword().equals(user.getPassword())) {
			return existingUser.get();
		}

		throw new RuntimeException("Invalid email or password");
	}

	@Override
	public User currentUser(User user) {
		Optional<User> currentUser = userRepo.findByEmail(user.getEmail());
		if (currentUser.isPresent()) {
			return currentUser.get();
		} else {
			throw new RuntimeException("User does not exist");
		}

	}

	@Override
	public User updateUser(User user) {
		Optional<User> currentUser = userRepo.findByEmail(user.getEmail());
		if (currentUser.isPresent()) {
			User existingUser = currentUser.get();

			if (user.getName() != null) {
				existingUser.setName(user.getName());
			}
			if (user.getPassword() != null) {
				existingUser.setPassword(user.getPassword());
			}
			if (user.getPhoneNumber() != null) {
				existingUser.setPhoneNumber(user.getPhoneNumber());
			}
			if (user.getRole() != null) {
				existingUser.setRole(user.getRole());
			}

			return userRepo.save(existingUser);
		} else {
			throw new RuntimeException("User does not exist");
		}
	}

	@Override
	public User deleteUser(User user) {
		Optional<User> currentUser = userRepo.findById(user.getId());
		if(currentUser.isPresent()) {
			userRepo.deleteById(user.getId());
			return currentUser.get();
		}
		else {
			throw new RuntimeException("No User exist with this id");
		}
	}

	@Override
	public User getUserById(String id) {
		Optional<User> user = userRepo.findById(Long.parseLong(id));
		if(user.isPresent()) {
			return user.get();
		}
		else {
			throw new RuntimeException("No User exist with this id");
		}
	}

	@Override
	public List<User> getUserByRole(String role) {
		return userRepo.findByRole(role);
	}
}

