package com.codefellows.xisbi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        XisbiUser xisbiUser = this.userRepo.findByUsername(username);
        if (xisbiUser == null) {
            System.out.println("XisbiUser not found " + username);
            throw new UsernameNotFoundException("Xisbi XisbiUser " + username + " was not found in the database");
        }
        System.out.println("Found Xisbi XisbiUser: " + xisbiUser);
        return xisbiUser;
    }
}
