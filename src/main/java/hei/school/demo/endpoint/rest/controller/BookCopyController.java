package hei.school.demo.endpoint.rest.controller;

import hei.school.demo.endpoint.rest.controller.dto.BookCopyCreationDto;
import hei.school.demo.endpoint.rest.controller.dto.BookCopyUpdate;
import hei.school.demo.entity.BookCopy;
import hei.school.demo.entity.enums.BookFormat;
import hei.school.demo.service.BookCopyService;
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
    try {
      BookCopy bookCopy = new BookCopy();
      bookCopy.setId(book.bookId());

      if (book.format() != null) {
        try {
          bookCopy.setFormat(BookFormat.valueOf(book.format().toUpperCase()));
        } catch (IllegalArgumentException e) {
          return ResponseEntity.status(400).body("Invalid enum format value.");
        }
      }

      bookCopy.setSellingPrice(book.sellingPrice().doubleValue());

      BookCopy createdCopy = copyService.save(bookCopy);

      return new ResponseEntity<>(createdCopy, HttpStatus.CREATED);

    } catch (Exception e) {
      return ResponseEntity.status(500).body("Crashing in controller: " + e.getMessage());
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getBookCopyById(@PathVariable String id) {
    BookCopy bookCopy = copyService.getCopyById(id);

    return ResponseEntity.ok(bookCopy);
  }

  @PatchMapping("/book-copies/{id}")
  public ResponseEntity<?> updateBookCopy(
      @PathVariable String id, @RequestBody BookCopyUpdate bookCopy) {
    BookCopy copy = copyService.updateBookCopy(id, bookCopy);
    return ResponseEntity.ok(copy);
  }

  @DeleteMapping("/book-copies/{id}")
  public ResponseEntity<?> deleteBookCopy(@PathVariable String id) {
    BookCopy bookCopy = copyService.deleteBookCopy(id);
    return ResponseEntity.ok(bookCopy);
  }
}
