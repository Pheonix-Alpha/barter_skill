package com.skillexchange.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skillexchange.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
    
}
