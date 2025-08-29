package com.srihari.contactverse.services;

import com.srihari.contactverse.entities.User;
import com.srihari.contactverse.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecurityCustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid email or password."));

        System.out.println("Loaded user: " + user.getEmail());
        System.out.println("Encoded password in DB: " + user.getPassword());
//        user.setEnabled(true);
        System.out.println("isEnabled: " + user.isEnabled());  // This should return true

        return user;
    }

}