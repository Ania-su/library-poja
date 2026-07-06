package hei.school.demo.repository;

import hei.school.demo.repository.model.JGenre;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<JGenre, String> {

  @Query(
      value =
          """
          SELECT
              g.name AS name,
              COALESCE(
                  SUM(
                      (si.quantity * si.unit_price)
                      - (bc.selling_price * si.quantity)
                  ),
                  0
              ) AS profit
          FROM sale_item si
          JOIN book_copy bc
              ON si.book_copy_id = bc.id
          JOIN book b
              ON bc.book_id = b.id
          JOIN book_genre bg
              ON b.id = bg.book_id
          RIGHT JOIN genre g
              ON bg.genre_id = g.id
          GROUP BY g.name
          """,
      nativeQuery = true)
  List<Object[]> getGenreProfits();
}
