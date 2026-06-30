package hei.school.demo.service;

import hei.school.demo.entity.AppUser;
import hei.school.demo.repository.UserRepository;
import hei.school.demo.repository.mapper.AppUserMapper;
import hei.school.demo.repository.model.JAppUser;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;
  private final AppUserMapper appUserMapper;

  @Override
  public AppUser loadUserByUsername(String username) throws UsernameNotFoundException {
    JAppUser jpaUser =  userRepository
        .findByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

    return appUserMapper.toDomain(jpaUser);
  }
}
