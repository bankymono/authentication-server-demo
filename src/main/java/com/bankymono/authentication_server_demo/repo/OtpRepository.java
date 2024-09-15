package com.bankymono.authentication_server_demo.repo;

import com.bankymono.authentication_server_demo.model.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, String> {
    Optional<Otp> findOtpByUsername(String username);
}
