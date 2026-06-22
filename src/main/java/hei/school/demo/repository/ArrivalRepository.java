package hei.school.demo.repository;

import hei.school.demo.repository.model.JArrival;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArrivalRepository extends JpaRepository<JArrival, UUID> {}
