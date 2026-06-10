package hei.school.demo.repository.specification;

import hei.school.demo.entity.BookCopy;
import hei.school.demo.entity.enums.BookFormat;
import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public class BookCopySpecifications {
  public static Specification<BookCopy> hasBookId(UUID bookId) {
    return (root, query, cb) ->
        bookId == null ? null : cb.equal(root.get("book").get("id"), bookId);
  }

  public static Specification<BookCopy> hasFormat(BookFormat format) {
    return (root, query, cb) -> format == null ? null : cb.equal(root.get("format"), format);
  }

  public static Specification<BookCopy> priceGreaterThanOrEqualTo(BigDecimal minPrice) {
    return (root, query, cb) ->
        minPrice == null ? null : cb.greaterThanOrEqualTo(root.get("sellingPrice"), minPrice);
  }

  public static Specification<BookCopy> priceLessThanOrEqualTo(BigDecimal maxPrice) {
    return (root, query, cb) ->
        maxPrice == null ? null : cb.lessThanOrEqualTo(root.get("sellingPrice"), maxPrice);
  }
}
