package hei.school.demo.service;

import hei.school.demo.endpoint.rest.controller.dto.BookCopyUpdate;
import hei.school.demo.entity.BookCopy;
import hei.school.demo.exception.NotFoundException;
import hei.school.demo.repository.BookCopyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BookCopyService {

  private final BookCopyRepository bookCopyRepository;

  public BookCopy updateBookCopy(String id, BookCopyUpdate bookCopy) {
    BookCopy existing =
        bookCopyRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("BookCopy not found with id : " + id));

    if (bookCopy.getFormat() != null) {
      existing.setFormat(bookCopy.getFormat());
    }
    if (bookCopy.getSellingPrice() != null) {
      existing.setSellingPrice(bookCopy.getSellingPrice());
    }
    return bookCopyRepository.save(existing);
  }
}
