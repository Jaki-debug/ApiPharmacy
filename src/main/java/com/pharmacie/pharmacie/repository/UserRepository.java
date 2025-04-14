package com.pharmacie.pharmacie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.pharmacie.pharmacie.model.User; // Vérifie cet import !

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
