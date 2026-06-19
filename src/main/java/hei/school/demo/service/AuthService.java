package hei.school.demo.service;

import hei.school.demo.entity.AppUser;
import hei.school.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  public String register(String email, String password) {
    if (email == null || email.isBlank() || password == null || password.isBlank()) {
      throw new InvalidCredentialsException("Email and password are required");
    }
    if (userRepository.findByEmail(email).isPresent()) {
      throw new EmailAlreadyTakenException("Email already taken");
    }

    AppUser user = new AppUser(null, email, passwordEncoder.encode(password));
    AppUser savedUser = userRepository.save(user);
    return jwtService.generateToken(savedUser);
  }

  public String login(String email, String password) {
    if (email == null || email.isBlank() || password == null || password.isBlank()) {
      throw new InvalidCredentialsException("Invalid credentials");
    }
    AppUser user =
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));

    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new InvalidCredentialsException("Invalid credentials");
    }

    return jwtService.generateToken(user);
  }
}
