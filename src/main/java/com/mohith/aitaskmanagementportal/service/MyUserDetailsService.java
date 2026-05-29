package com.mohith.aitaskmanagementportal.service;

import com.mohith.aitaskmanagementportal.dao.UsersRepo;
import com.mohith.aitaskmanagementportal.model.UserPrincipal;
import com.mohith.aitaskmanagementportal.model.Users;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UsersRepo usersRepo;

    @Override
    public UserDetails loadUserByUsername(@NotNull String username) throws UsernameNotFoundException {
        Optional<Users> user = usersRepo.findById(username);
        if(user.isEmpty()){
            System.out.println("404 user not found");
            throw new UsernameNotFoundException("user not found");
        }
        return new UserPrincipal(user.get());
    }
}
