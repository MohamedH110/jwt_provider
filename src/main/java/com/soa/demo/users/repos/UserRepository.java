package com.soa.demo.users.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.soa.demo.entites.User;

public interface UserRepository extends JpaRepository<User, Long> {

		User findByUsername(String username);

}