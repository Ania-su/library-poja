package hei.school.demo.repository.specification;

import hei.school.demo.entity.enums.BookFormat;
import hei.school.demo.repository.model.JBookCopy;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public class BookCopySpecifications {
  public static Specification<JBookCopy> hasBookId(UUID bookId) {
    return (root, query, cb) ->
        bookId == null ? null : cb.equal(root.get("book").get("id"), bookId);
  }

  public static Specification<JBookCopy> hasFormat(BookFormat format) {
    return (root, query, cb) -> format == null ? null : cb.equal(root.get("format"), format);
  }

  public static Specification<JBookCopy> priceGreaterThanOrEqualTo(Double minPrice) {
    return (root, query, cb) ->
        minPrice == null ? null : cb.greaterThanOrEqualTo(root.get("sellingPrice"), minPrice);
  }

  public static Specification<JBookCopy> priceLessThanOrEqualTo(Double maxPrice) {
    return (root, query, cb) ->
        maxPrice == null ? null : cb.lessThanOrEqualTo(root.get("sellingPrice"), maxPrice);
  }
}
