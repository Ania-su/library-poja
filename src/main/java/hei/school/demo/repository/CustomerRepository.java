package hei.school.demo.repository;

import hei.school.demo.repository.model.JCustomer;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<JCustomer, UUID> {}
