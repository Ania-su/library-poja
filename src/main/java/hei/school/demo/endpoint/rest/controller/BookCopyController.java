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
@RequestMapping("/books/{id}/copy")
@RequiredArgsConstructor
public class BookCopyController {
  private final BookCopyService copyService;

  @GetMapping
  public ResponseEntity<List<BookCopy>> getAllBookCopies(
      @PathVariable UUID id,
      @RequestParam(required = false) BookFormat format,
      @RequestParam(required = false) Double minPrice,
      @RequestParam(required = false) Double maxPrice) {
    return ResponseEntity.ok(copyService.findAll(id, format, minPrice, maxPrice));
  }

  @PostMapping
  public ResponseEntity<?> createBookCopy(@RequestBody BookCopyCreationDto book) {
    BookCopy createdCopy = copyService.save(book);

    return new ResponseEntity<>(createdCopy, HttpStatus.CREATED);
  }

  @GetMapping("/{copyId}/stock")
  public ResponseEntity<BookStockResponse> getStock(
      @PathVariable UUID copyId, @RequestParam(required = false) LocalDate t) {
    return ResponseEntity.ok(copyService.calculateStock(copyId, t));
  }

  @GetMapping("/{copyId}")
  public ResponseEntity<?> getBookCopyById(@PathVariable UUID copyId) {
    BookCopy bookCopy = copyService.getCopyById(copyId);
    return ResponseEntity.ok(bookCopy);
  }

  @PatchMapping("/{copyId}")
  public ResponseEntity<?> updateBookCopy(
      @PathVariable UUID copyId, @RequestBody BookCopyUpdate bookCopy) {
    BookCopy copy = copyService.updateBookCopy(copyId, bookCopy);
    return ResponseEntity.ok(copy);
  }

  @DeleteMapping("/{copyId}")
  public ResponseEntity<?> deleteBookCopy(@PathVariable UUID copyId) {
    BookCopy bookCopy = copyService.deleteBookCopy(copyId);
    return ResponseEntity.ok(bookCopy);
  }

  @GetMapping("/stock")
  public ResponseEntity<BookStockResponse> getTotalStock(
      @PathVariable UUID id, @RequestParam(required = false) LocalDate date) {
    return ResponseEntity.ok(copyService.calculateTotalStock(id, date));
  }
}
