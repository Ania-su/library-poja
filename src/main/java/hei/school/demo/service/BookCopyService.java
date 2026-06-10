package hei.school.demo.service;

import hei.school.demo.endpoint.rest.controller.dto.BookCopyUpdate;
import hei.school.demo.entity.Book;
import hei.school.demo.entity.BookCopy;
import hei.school.demo.entity.enums.BookFormat;
import hei.school.demo.exception.NotFoundException;
import hei.school.demo.repository.BookCopyRepository;
import hei.school.demo.repository.BookRepository;
import hei.school.demo.repository.specification.BookCopySpecifications;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BookCopyService {

  private final BookCopyRepository bookCopyRepository;
  private final BookRepository bookRepository;

  public List<BookCopy> findAll(
      UUID bookId, BookFormat format, BigDecimal minPrice, BigDecimal maxPrice) {
    Specification<BookCopy> spec =
        Specification.where(BookCopySpecifications.hasBookId(bookId))
            .and(BookCopySpecifications.hasFormat(format))
            .and(BookCopySpecifications.priceGreaterThanOrEqualTo(minPrice))
            .and(BookCopySpecifications.priceLessThanOrEqualTo(maxPrice));

    return bookCopyRepository.findAll(spec);
  }

  public BookCopy save(BookCopy bookCopy) {
    if (bookCopy.getSellingPrice() != null
        && bookCopy.getSellingPrice().compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Selling price cannot be negative.");
    }

    Book parentBook =
        bookRepository
            .findById(bookCopy.getId())
            .orElseThrow(
                () -> new IllegalArgumentException("Book not found with ID: " + bookCopy.getId()));

    BookCopy bookCp = new BookCopy();
    bookCp.setBook(parentBook);
    bookCp.setFormat(bookCopy.getFormat());
    bookCp.setSellingPrice(bookCopy.getSellingPrice());

    return bookCopyRepository.save(bookCp);
  }

  public BookCopy updateBookCopy(String id, BookCopyUpdate bookCopy) {
    BookCopy existing =
        bookCopyRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("BookCopy not found with id : " + id));

    if (bookCopy.getFormat() != null) {
      existing.setFormat(bookCopy.getFormat());
    }
    if (bookCopy.getSellingPrice() != null) {
      existing.setSellingPrice(BigDecimal.valueOf(bookCopy.getSellingPrice()));
    }
    return bookCopyRepository.save(existing);
  }

  public BookCopy deleteBookCopy(String id) {
    BookCopy bookCopy =
        bookCopyRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("BookCopy not found with id : " + id));
    bookCopyRepository.delete(bookCopy);
    return bookCopy;
  }
}
