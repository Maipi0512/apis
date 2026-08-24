package com.uade.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.ecom.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
