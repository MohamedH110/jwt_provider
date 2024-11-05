package com.soa.demo.users.service;

import java.util.List;

import com.soa.demo.entites.Role;
import com.soa.demo.entites.User;

public interface UserService {
	User saveUser(User user);
	User findUserByUsername (String username);
	Role addRole(Role role);
	User addRoleToUser(String username, String rolename);
	List<User> findAllUsers();

}
