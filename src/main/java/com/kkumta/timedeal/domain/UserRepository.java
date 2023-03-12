package com.kkumta.timedeal.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    @Override
    Optional<User> findById(Long userId);
    
    Optional<User> findByName(String name);
    
    Optional<User> findByEmail(String email);
    
}