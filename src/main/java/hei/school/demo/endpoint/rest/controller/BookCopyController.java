package hei.school.demo.endpoint.rest.controller;

import hei.school.demo.endpoint.rest.controller.dto.BookCopyCreationDto;
import hei.school.demo.endpoint.rest.controller.dto.BookCopyUpdate;
import hei.school.demo.endpoint.rest.controller.dto.BookStockResponse;
import hei.school.demo.entity.BookCopy;
import hei.school.demo.entity.enums.BookFormat;
import hei.school.demo.service.BookCopyService;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book-copies")
@RequiredArgsConstructor
public class BookCopyController {
  private final BookCopyService copyService;

  @GetMapping
  public ResponseEntity<List<BookCopy>> getAllBookCopies(
      @RequestParam(required = false) UUID bookId,
      @RequestParam(required = false) BookFormat format,
      @RequestParam(required = false) Double minPrice,
      @RequestParam(required = false) Double maxPrice) {
    return ResponseEntity.ok(copyService.findAll(bookId, format, minPrice, maxPrice));
  }

  @PostMapping
  public ResponseEntity<?> createBookCopy(@RequestBody BookCopyCreationDto book) {
    BookCopy createdCopy = copyService.save(book);

    return new ResponseEntity<>(createdCopy, HttpStatus.CREATED);
  }

  @GetMapping("/{id}/stock")
  public ResponseEntity<BookStockResponse> getStock(
      @PathVariable UUID id, @RequestParam(required = false) LocalDate t) {
    return ResponseEntity.ok(copyService.calculateStock(id, t));
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getBookCopyById(@PathVariable UUID id) {
    BookCopy bookCopy = copyService.getCopyById(id);
    return ResponseEntity.ok(bookCopy);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<?> updateBookCopy(
      @PathVariable UUID id, @RequestBody BookCopyUpdate bookCopy) {
    BookCopy copy = copyService.updateBookCopy(id, bookCopy);
    return ResponseEntity.ok(copy);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteBookCopy(@PathVariable UUID id) {
    BookCopy bookCopy = copyService.deleteBookCopy(id);
    return ResponseEntity.ok(bookCopy);
  }
}
