package com.mit.StayNest.Services;

import com.mit.StayNest.Entity.Owner;
import com.mit.StayNest.Repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class OwnerDetailsService implements UserDetailsService {

    @Autowired
    private OwnerRepository ownerRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Owner owner = ownerRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Owner not found with email: " + email));

        return new org.springframework.security.core.userdetails.User(
                owner.getEmail(),
                owner.getPassword(),
                new ArrayList<>()
        );
    }
}
