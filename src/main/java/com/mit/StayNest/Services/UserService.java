package com.mit.StayNest.Services;

import java.util.List;

import org.springframework.stereotype.Service;
import com.mit.StayNest.Entity.User;


@Service
public interface UserService {
	public List<User> getUser();

	User register(User user);

	User login(User user);
}