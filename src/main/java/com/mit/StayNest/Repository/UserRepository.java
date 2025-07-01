package com.mit.StayNest.Repository;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mit.StayNest.Entity.User;

public interface UserRepository extends JpaRepository<User,Long>{
	   Optional<User> findByEmail(String email);
}