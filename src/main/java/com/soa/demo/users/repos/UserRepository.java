package com.soa.demo.users.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.soa.demo.entites.User;

public interface UserRepository extends JpaRepository<User, Long> {

		User findByUsername(String username);
		Optional<User> findByEmail(String email);

}