package com.soa.demo.users.service;

import java.util.List;

import com.soa.demo.entites.Role;
import com.soa.demo.entites.User;
import com.soa.demo.users.service.resgister.RegistrationRequest;

public interface UserService {
	User saveUser(User user);
	User findUserByUsername (String username);
	Role addRole(Role role);
	User addRoleToUser(String username, String rolename);
	List<User> findAllUsers();
	
	User registerUser(RegistrationRequest request);
	public void sendEmailUser(User u, String code);
	public User validateToken(String code);

}
