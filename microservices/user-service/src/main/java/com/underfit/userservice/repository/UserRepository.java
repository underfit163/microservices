package com.underfit.userservice.repository;

import com.underfit.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> getUsersByCompanyId(Long companyId);
}