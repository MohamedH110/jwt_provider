package com.soa.demo.users.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.soa.demo.entites.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Role findByRole(String role);
	
}