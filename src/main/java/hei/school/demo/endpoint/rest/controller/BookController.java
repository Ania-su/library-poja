package hei.school.demo.endpoint.rest.controller;

import hei.school.demo.endpoint.rest.controller.dto.BookRequest;
import hei.school.demo.endpoint.rest.controller.dto.BooksResponse;
import hei.school.demo.endpoint.rest.controller.dto.PaginationMeta;
import hei.school.demo.entity.Book;
import hei.school.demo.service.BookService;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class BookController {

  private final BookService bookService;

  @GetMapping("/books")
  public ResponseEntity<?> getBooks(
      @RequestParam(name = "title", required = false) String title,
      @RequestParam(name = "description", required = false) String description,
      @RequestParam(name = "authorId", required = false) List<String> authorId,
      @RequestParam(name = "genreId", required = false) List<String> genreId,
      @RequestParam(name = "before", required = false) LocalDate before,
      @RequestParam(name = "after", required = false) LocalDate after,
      @RequestParam(name = "page", defaultValue = "1") int page,
      @RequestParam(name = "perPage", defaultValue = "10") int perPage) {

    List<Book> books =
        bookService.getBooks(title, description, before, authorId, genreId, after, page, perPage);
    long total = bookService.countBooks(title, description, authorId, genreId, before, after);

    BooksResponse response = new BooksResponse(books, new PaginationMeta(total, page, perPage));

    return ResponseEntity.ok(response);
  }

  @PostMapping("/books")
  public ResponseEntity<?> createBook(@RequestBody BookRequest book) {
    Book created = bookService.createBook(book);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @GetMapping("/books/{id}")
  public ResponseEntity<?> getBookById(@PathVariable UUID id) {
    Book book = bookService.getBookById(id);
    return ResponseEntity.ok(book);
  }

  @PutMapping("/books/{id}")
  public ResponseEntity<?> updateBook(@PathVariable UUID id, @RequestBody BookRequest book) {
    Book updated = bookService.updateBook(id, book);
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/book/{id}")
  public ResponseEntity<?> deleteBook(@PathVariable UUID id) {
    bookService.deleteBook(id);
    return ResponseEntity.noContent().build();
  }
}
