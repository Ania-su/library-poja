package hei.school.demo.service;

import hei.school.demo.endpoint.rest.controller.dto.BookCopyCreationDto;
import hei.school.demo.endpoint.rest.controller.dto.BookCopyUpdate;
import hei.school.demo.endpoint.rest.controller.dto.BookStockResponse;
import hei.school.demo.entity.BookCopy;
import hei.school.demo.entity.enums.BookFormat;
import hei.school.demo.exception.NotFoundException;
import hei.school.demo.repository.ArrivalRepository;
import hei.school.demo.repository.BookCopyRepository;
import hei.school.demo.repository.BookRepository;
import hei.school.demo.repository.SaleRepository;
import hei.school.demo.repository.mapper.BookCopyMapper;
import hei.school.demo.repository.model.JArrivalItem;
import hei.school.demo.repository.model.JBook;
import hei.school.demo.repository.model.JBookCopy;
import hei.school.demo.repository.model.JSaleItem;
import hei.school.demo.repository.specification.BookCopySpecifications;
import java.time.LocalDate;
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
  private final ArrivalRepository arrivalRepository;
  private final SaleRepository saleRepository;
  private final BookCopyMapper bookCopyMapper;

  public List<BookCopy> findAll(UUID bookId, BookFormat format, Double minPrice, Double maxPrice) {
    Specification<JBookCopy> spec =
        Specification.where(BookCopySpecifications.hasBookId(bookId))
            .and(BookCopySpecifications.hasFormat(format))
            .and(BookCopySpecifications.priceGreaterThanOrEqualTo(minPrice))
            .and(BookCopySpecifications.priceLessThanOrEqualTo(maxPrice));

    List<JBookCopy> jBookCopies = bookCopyRepository.findAll(spec);
    return bookCopyMapper.toDomain(jBookCopies);
  }

  public BookCopy save(BookCopyCreationDto book) {

    BookCopy bookCopy = new BookCopy();
    bookCopy.setId(book.getBookId());

    if (book.getFormat() != null) {
      bookCopy.setFormat(BookFormat.valueOf(book.getFormat().toUpperCase()));
    }

    bookCopy.setSellingPrice(book.getSellingPrice().doubleValue());

    if (bookCopy.getSellingPrice() < 0) {
      throw new IllegalArgumentException("Selling price cannot be negative.");
    }

    JBook parentBook =
        bookRepository
            .findById(bookCopy.getId())
            .orElseThrow(
                () -> new IllegalArgumentException("Book not found with ID: " + bookCopy.getId()));

    JBookCopy jBookCopy = new JBookCopy();
    jBookCopy.setBook(parentBook);
    jBookCopy.setFormat(bookCopy.getFormat());
    jBookCopy.setSellingPrice(bookCopy.getSellingPrice());

    JBookCopy saved = bookCopyRepository.save(jBookCopy);
    return bookCopyMapper.toDomain(saved);
  }

  public BookCopy getCopyById(UUID id) {
    JBookCopy jBookCopy =
        bookCopyRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Book copy not found"));
    return bookCopyMapper.toDomain(jBookCopy);
  }

  public BookCopy updateBookCopy(UUID id, BookCopyUpdate bookCopy) {
    JBookCopy existing =
        bookCopyRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("BookCopy not found with id : " + id));

    if (bookCopy.getFormat() != null) {
      existing.setFormat(bookCopy.getFormat());
    }
    if (bookCopy.getSellingPrice() != null) {
      existing.setSellingPrice(bookCopy.getSellingPrice());
    }
    JBookCopy saved = bookCopyRepository.save(existing);
    return bookCopyMapper.toDomain(saved);
  }

  public BookStockResponse calculateStock(UUID id, LocalDate date) {
    bookCopyRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException("BookCopy not found with id: " + id));

    int arrivalQty;
    int saleQty;

    if (date == null) {
      arrivalQty = arrivalRepository.sumArrivalQuantityByBookCopyId(id);
      saleQty = saleRepository.sumSaleQuantityByBookCopyId(id);
    } else {
      arrivalQty =
          arrivalRepository.findArrivalItemsByBookCopyIdAndDateBeforeEqual(id, date).stream()
              .mapToInt(JArrivalItem::getQuantity)
              .sum();
      saleQty =
          saleRepository.findSaleItemsByBookCopyIdAndDateBeforeEqual(id, date).stream()
              .mapToInt(JSaleItem::getQuantity)
              .sum();
    }

    return new BookStockResponse(id, arrivalQty - saleQty);
  }

  public BookCopy deleteBookCopy(UUID id) {
    JBookCopy jBookCopy =
        bookCopyRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("BookCopy not found with id : " + id));
    bookCopyRepository.delete(jBookCopy);
    return bookCopyMapper.toDomain(jBookCopy);
  }
}
