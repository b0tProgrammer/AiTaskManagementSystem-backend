package com.mohith.aitaskmanagementportal.dao;

import com.mohith.aitaskmanagementportal.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepo extends JpaRepository<Users,String> {
}
