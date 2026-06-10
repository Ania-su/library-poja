package hei.school.demo.endpoint.rest.controller;

import hei.school.demo.endpoint.rest.controller.dto.BookCopyUpdate;
import hei.school.demo.entity.BookCopy;
import hei.school.demo.repository.BookCopyRepository;
import hei.school.demo.service.BookCopyService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class BookCopyController {
  private final BookCopyRepository bookCopyRepository;
  private final BookCopyService bookCopyService;

  @PatchMapping("/book-copies/{id}")
  public ResponseEntity<?> updateBookCopy(
      @PathVariable String id, @RequestBody BookCopyUpdate bookCopy) {
    BookCopy copy = bookCopyService.updateBookCopy(id, bookCopy);
    return ResponseEntity.ok(copy);
  }
}
