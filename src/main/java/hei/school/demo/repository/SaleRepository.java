package hei.school.demo.repository;

import hei.school.demo.repository.model.JSale;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepository extends JpaRepository<JSale, UUID> {

  @Query(
      "SELECT COALESCE(SUM(si.quantity), 0) FROM JSaleItem si"
          + " WHERE si.bookCopy.id = :bookCopyId AND si.sale.status <> 'CANCELLED'")
  int sumSaleQuantityByBookCopyId(UUID bookCopyId);
}
