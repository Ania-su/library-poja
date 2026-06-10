package hei.school.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hei.school.demo.entity.AppUser;

public interface UserRepository extends JpaRepository<AppUser, String>{
    Optional<AppUser> findByEmail(String email);
}
