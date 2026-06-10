package hei.school.demo.endpoint.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import hei.school.demo.dto.RegisterRequest;
import hei.school.demo.service.AuthService;
import hei.school.demo.service.EmailAlreadyTakenException;
import hei.school.demo.service.InvalidCredentialsException;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        try {
            String token = authService.register(request.getEmail(), request.getPassword());
            return ResponseEntity.ok(token);
        } catch (EmailAlreadyTakenException e) {
            return ResponseEntity.status(409).body("Email already taken");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody RegisterRequest request) {
        try {
            String token = authService.login(request.getEmail(), request.getPassword());
            return ResponseEntity.ok(token);
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}
