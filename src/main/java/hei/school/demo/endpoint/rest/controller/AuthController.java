package hei.school.demo.endpoint.rest.controller;

import hei.school.demo.dto.RegisterRequest;
import hei.school.demo.service.AuthService;
import hei.school.demo.service.EmailAlreadyTakenException;
import hei.school.demo.service.InvalidCredentialsException;
import lombok.AllArgsConstructor;

import java.time.Duration;
import org.springframework.http.HttpHeaders;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
    try {

      String token = authService.register(request.getEmail(), request.getPassword());
      
      ResponseCookie cookie = ResponseCookie.from("jwt", token)
      .httpOnly(true)
      .secure(true)
      .path("/")
      .maxAge(Duration.ofHours(1))
      .sameSite("Strict")
      .build();
      
      return ResponseEntity.status(201)
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .header("Content-type", "text/plain")
            .body("Register successfully");
      
    } catch (EmailAlreadyTakenException e) {
      return ResponseEntity.status(409).body("Email already taken");
    } catch (InvalidCredentialsException e) {
      return ResponseEntity.status(400).body("Email and password are required");
    }
  }

  @PostMapping("/login")
  public ResponseEntity<String> login(@RequestBody RegisterRequest request) {
    try {
      String token = authService.login(request.getEmail(), request.getPassword());

      ResponseCookie cookie = ResponseCookie.from("jwt", token)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(Duration.ofHours(1))
            .sameSite("Strict")
            .build();
            
      return ResponseEntity.status(200)
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .header("Content-type", "text/plain")
            .body("Login successfully");

    } catch (InvalidCredentialsException e) {
      return ResponseEntity.status(401).body("Invalid credentials");
    }
  }
}
