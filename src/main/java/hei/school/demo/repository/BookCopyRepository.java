package hei.school.demo.repository;

import hei.school.demo.repository.model.JBookCopy;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookCopyRepository
    extends JpaRepository<JBookCopy, UUID>, JpaSpecificationExecutor<JBookCopy> {

  @Query("SELECT b.id, b.title, bc.id, bc.format, " +
         "(SELECT COALESCE(SUM(ai.quantity), 0) FROM JArrivalItem ai WHERE ai.bookCopy.id = bc.id) - " +
         "(SELECT COALESCE(SUM(si.quantity), 0) FROM JSaleItem si WHERE si.bookCopy.id = bc.id) " +
         "FROM JBookCopy bc JOIN bc.book b " +
         "WHERE (SELECT COALESCE(SUM(ai.quantity), 0) FROM JArrivalItem ai WHERE ai.bookCopy.id = bc.id) - " +
         "(SELECT COALESCE(SUM(si.quantity), 0) FROM JSaleItem si WHERE si.bookCopy.id = bc.id) < :threshold")
  List<Object[]> findLowStockCopies(@Param("threshold") int threshold);
}
