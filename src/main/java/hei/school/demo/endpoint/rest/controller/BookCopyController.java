package hei.school.demo.endpoint.rest.controller;

import hei.school.demo.entity.BookCopy;
import hei.school.demo.entity.enums.BookFormat;
import hei.school.demo.service.BookCopyService;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/book-copies")
@AllArgsConstructor
public class BookCopyController {
  private final BookCopyService copyService;

  @GetMapping
  public ResponseEntity<List<BookCopy>> getAllBookCopies(
      @RequestParam(required = false) UUID bookId,
      @RequestParam(required = false) BookFormat format,
      @RequestParam(required = false) BigDecimal minPrice,
      @RequestParam(required = false) BigDecimal maxPrice) {
    return ResponseEntity.ok(copyService.findAll(bookId, format, minPrice, maxPrice));
  }
}
