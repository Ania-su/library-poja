package hei.school.demo.repository;

import hei.school.demo.repository.model.JAppUser;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<JAppUser, UUID> {
  Optional<JAppUser> findByEmail(String email);
}
