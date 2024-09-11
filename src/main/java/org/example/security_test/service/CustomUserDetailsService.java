package org.example.security_test.service;

import org.example.security_test.dto.CustomUserDetails;
import org.example.security_test.entity.UserEntity;
import org.example.security_test.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userData = userRepo.findByUsername(username);

        if(userData != null){
            return new CustomUserDetails(userData);
        }
        return null;
    }
}
