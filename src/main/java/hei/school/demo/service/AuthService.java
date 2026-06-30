package hei.school.demo.service;

import hei.school.demo.endpoint.rest.controller.dto.RegisterRequest;
import hei.school.demo.entity.AppUser;
import hei.school.demo.repository.UserRepository;
import hei.school.demo.repository.mapper.AppUserMapper;
import hei.school.demo.repository.model.JAppUser;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AppUserMapper appUserMapper;

  public String register(RegisterRequest userToSave) {
    if (userToSave.getEmail() == null
        || userToSave.getEmail().isBlank()
        || userToSave.getPassword() == null
        || userToSave.getPassword().isBlank()) {
      throw new InvalidCredentialsException("Email and password are required");
    }
    if (userRepository.findByEmail(userToSave.getEmail()).isPresent()) {
      throw new EmailAlreadyTakenException("Email already taken");
    }

    JAppUser user =
        new JAppUser(
            null,
            userToSave.getEmail(),
            passwordEncoder.encode(userToSave.getPassword()),
            userToSave.getRole());
    JAppUser saved = userRepository.save(user);

    return jwtService.generateToken(appUserMapper.toDomain(saved));
  }

  public String login(String email, String password) {

    if (email == null || email.isBlank() || password == null || password.isBlank()) {
      throw new InvalidCredentialsException("Invalid credentials");
    }

    JAppUser user =
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));

    AppUser appUser = appUserMapper.toDomain(user);

    if (!passwordEncoder.matches(password, appUser.getPasswordHash())) {
      throw new InvalidCredentialsException("Invalid credentials");
    }

    return jwtService.generateToken(appUser);
  }
}
