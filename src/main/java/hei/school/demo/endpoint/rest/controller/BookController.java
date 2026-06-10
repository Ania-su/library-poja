package hei.school.demo.endpoint.rest.controller;

import hei.school.demo.endpoint.rest.controller.dto.BookRequest;
import hei.school.demo.endpoint.rest.controller.dto.BooksResponse;
import hei.school.demo.entity.Book;
import hei.school.demo.service.BookService;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class BookController {

  private final BookService bookService;

  @GetMapping("/books")
  public ResponseEntity<?> getBooks(
      @RequestParam(name = "title", required = false) String title,
      @RequestParam(name = "description", required = false) String description,
      @RequestParam(name = "authorId", required = false) String authorId,
      @RequestParam(name = "genreId", required = false) String genreId,
      @RequestParam(name = "before", required = false) LocalDate before,
      @RequestParam(name = "after", required = false) LocalDate after,
      @RequestParam(name = "page", defaultValue = "1") int page,
      @RequestParam(name = "perPage", defaultValue = "10") int perPage) {

    try {
      List<Book> books =
          bookService.getBooks(title, description, before, authorId, genreId, after, page, perPage);
      long total = bookService.countBooks(title, description, authorId, genreId, before, after);

      BooksResponse response =
          new BooksResponse(books, new BooksResponse.Meta(total, page, perPage));

      return ResponseEntity.ok(response);
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

  @PostMapping("/books")
  public ResponseEntity<?> createBook(@RequestBody BookRequest book) {
    try {
      Book created = bookService.createBook(book);
      return ResponseEntity.status(HttpStatus.CREATED).body(created);
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

  @GetMapping("/books/{id}")
  public ResponseEntity<?> getBookById(@PathVariable String id) {
    try {
      Book book = bookService.getBookById(id);
      return ResponseEntity.ok(book);
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

  @PutMapping("/books/{id}")
  public ResponseEntity<?> updateBook(@PathVariable String id, @RequestBody BookRequest book) {
    try {
      Book updated = bookService.updateBook(id, book);
      return ResponseEntity.ok(updated);
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }
}
