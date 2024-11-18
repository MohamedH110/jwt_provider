package com.soa.demo.users.service.resgister;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
 VerificationToken findByToken(String token);
}