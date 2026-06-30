package hei.school.demo.repository;

import hei.school.demo.repository.model.JArrival;
import hei.school.demo.repository.model.JArrivalItem;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArrivalRepository extends JpaRepository<JArrival, UUID> {

  @Query(
      "SELECT COALESCE(SUM(ai.quantity), 0) FROM JArrivalItem ai WHERE ai.bookCopy.id ="
          + " :bookCopyId")
  int sumArrivalQuantityByBookCopyId(UUID bookCopyId);

  @Query(
      "SELECT ai FROM JArrivalItem ai WHERE ai.bookCopy.id = :bookCopyId AND ai.arrival.arrivalDate"
          + " <= :date")
  List<JArrivalItem> findArrivalItemsByBookCopyIdAndDateBeforeEqual(
      UUID bookCopyId, LocalDate date);
}
