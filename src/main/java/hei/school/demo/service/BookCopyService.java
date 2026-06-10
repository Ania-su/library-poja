package hei.school.demo.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import hei.school.demo.entity.BookCopy;
import hei.school.demo.entity.enums.BookFormat;
import hei.school.demo.repository.BookCopyRepository;
import hei.school.demo.repository.specification.BookCopySpecifications;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BookCopyService {

  private final BookCopyRepository bookCopyRepository;

  public List<BookCopy> findAll(UUID bookId, BookFormat format, BigDecimal minPrice, BigDecimal maxPrice) {
    Specification<BookCopy> spec = Specification.where(BookCopySpecifications.hasBookId(bookId))
        .and(BookCopySpecifications.hasFormat(format))
        .and(BookCopySpecifications.priceGreaterThanOrEqualTo(minPrice))
        .and(BookCopySpecifications.priceLessThanOrEqualTo(maxPrice));

    return bookCopyRepository.findAll(spec);
  }
}
