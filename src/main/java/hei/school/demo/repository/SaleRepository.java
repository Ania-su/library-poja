package hei.school.demo.repository;

import hei.school.demo.repository.model.JSale;
import hei.school.demo.repository.model.JSaleItem;
import java.time.LocalDate;
import java.util.List;
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

  @Query(
      "SELECT si FROM JSaleItem si WHERE si.bookCopy.id = :bookCopyId AND si.sale.saleDate <= :date"
          + " AND si.sale.status <> 'CANCELLED'")
  List<JSaleItem> findSaleItemsByBookCopyIdAndDateBeforeEqual(UUID bookCopyId, LocalDate date);
}
