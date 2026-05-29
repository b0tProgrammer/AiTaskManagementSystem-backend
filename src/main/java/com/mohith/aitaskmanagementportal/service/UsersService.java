package com.mohith.aitaskmanagementportal.service;

import com.mohith.aitaskmanagementportal.dao.UsersRepo;
import com.mohith.aitaskmanagementportal.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersService {
    @Autowired
    private UsersRepo usersRepo;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public boolean register(Users user) {
        user.setPassword(encoder.encode(user.getPassword()));
        usersRepo.save(user);
        return true;
    }

    public boolean login(Users user) {
        return usersRepo.existsById(user.getUsername());
    }
}
