package com.pharmacie.pharmacie.controller;

import com.pharmacie.pharmacie.dto.UserDTO;
import com.pharmacie.pharmacie.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO userDTO) {
        try {
            userService.registerUser(userDTO);
            return ResponseEntity.ok("Inscription réussie !");
        } catch (IllegalArgumentException e) {
            // retourn une erreur si l'email est déjà pris
            return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
        }
    }
}
