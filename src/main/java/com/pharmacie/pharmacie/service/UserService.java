package com.pharmacie.pharmacie.service;

import com.pharmacie.pharmacie.dto.UserDTO;
import com.pharmacie.pharmacie.model.User;
import com.pharmacie.pharmacie.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO registerUser(UserDTO userDTO) {
   
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Cet email est déjà utilisé !");
        }

        // Cré nouvel utilisateur
        User newUser = new User();
        newUser.setEmail(userDTO.getEmail());
        newUser.setPassword(userDTO.getPassword());  

       
        User savedUser = userRepository.save(newUser);
        return new UserDTO(savedUser.getEmail(), null, savedUser.getRole()); 
    }
}
