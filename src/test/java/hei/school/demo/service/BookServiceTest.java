package hei.school.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import hei.school.demo.endpoint.rest.controller.dto.BookRequest;
import hei.school.demo.entity.Book;
import hei.school.demo.repository.BookRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
  // @Mock
  // private AuthorRepository authorRepository;
  // @Mock
  // private GenreRepository genreRepository;
  @Mock private BookRepository bookRepository;

  @InjectMocks private BookService bookService;

  private Book book1;
  private Book book2;

  @BeforeEach
  void setUp() {
    book1 =
        new Book(
            "book-uuid-001",
            "Méthode Boscher",
            LocalDate.of(2008, 8, 1),
            "Livre éducatif pour apprendre le français",
            null,
            null,
            null);
    book2 =
        new Book(
            "book-uuid-002",
            "Atomic Habits",
            LocalDate.of(2012, 10, 20),
            "Change your life with consistency",
            null,
            null,
            null);
  }

  @Test
  void getBooks_shouldReturnAllBooks_whenNoFilterProvided() {
    when(bookRepository.findAll(any(Specification.class), eq(PageRequest.of(0, 10))))
        .thenReturn(new PageImpl<>(List.of(book1, book2)));

    List<Book> result = bookService.getBooks(null, null, null, null, null, null, 1, 10);

    assertEquals(2, result.size());
    verify(bookRepository).findAll(any(Specification.class), eq(PageRequest.of(0, 10)));
  }

  @Test
  void getBooks_shouldReturnEmptyList_whenNoBooksMatchFilter() {
    when(bookRepository.findAll(any(Specification.class), eq(PageRequest.of(0, 10))))
        .thenReturn(new PageImpl<>(List.of()));

    List<Book> result = bookService.getBooks("Inexistant", null, null, null, null, null, 1, 10);

    assertTrue(result.isEmpty());
  }

  @Test
  void getBooks_shouldRespectPagination_whenPageAndPerPageProvided() {
    when(bookRepository.findAll(any(Specification.class), eq(PageRequest.of(1, 5))))
        .thenReturn(new PageImpl<>(List.of(book2)));

    List<Book> result = bookService.getBooks(null, null, null, null, null, null, 2, 5);

    assertEquals(1, result.size());
    verify(bookRepository).findAll(any(Specification.class), eq(PageRequest.of(1, 5)));
  }

  @Test
  void countBooks_shouldReturnTotalCount_whenNoFilterProvided() {
    when(bookRepository.count(any(Specification.class))).thenReturn(2L);

    long count = bookService.countBooks(null, null, null, null, null, null);

    assertEquals(2L, count);
  }

  @Test
  void testCreateBook() {
    BookRequest request = new BookRequest();
    request.setTitle("Le Petit Prince");
    request.setDescription("Un classique");
    request.setPublicationDate(LocalDate.of(1943, 4, 6));

    Book saved = new Book();
    saved.setId("generated-uuid");
    saved.setTitle("Le Petit Prince");
    saved.setDescription("Un classique");
    saved.setPublicationDate(LocalDate.of(1943, 4, 6));

    when(bookRepository.save(any(Book.class))).thenReturn(saved);

    Book result = bookService.createBook(request);

    assertNotNull(result);
    assertEquals("generated-uuid", result.getId());
    assertEquals("Le Petit Prince", result.getTitle());
    assertEquals("Un classique", result.getDescription());
    verify(bookRepository, times(1)).save(any(Book.class));
  }

  @Test
  void testGetBookById() {
    Book book = new Book();
    book.setId("123");
    book.setTitle("1984");

    when(bookRepository.findById("123")).thenReturn(Optional.of(book));

    Book result = bookService.getBookById("123");

    assertNotNull(result);
    assertEquals("123", result.getId());
    assertEquals("1984", result.getTitle());
    verify(bookRepository, times(1)).findById("123");
  }

  @Test
  void testGetBookById_notFound() {
    when(bookRepository.findById("unknown")).thenReturn(Optional.empty());

    RuntimeException ex =
        assertThrows(RuntimeException.class, () -> bookService.getBookById("unknown"));

    assertTrue(ex.getMessage().contains("Book not found"));
  }

  @Test
  void testUpdateBook() {
    Book existing = new Book();
    existing.setId("uuid-123");
    existing.setTitle("Ancien titre");

    BookRequest request = new BookRequest();
    request.setTitle("Nouveau titre");
    request.setDescription("Nouvelle description");
    request.setPublicationDate(LocalDate.of(2000, 1, 1));

    Book updated = new Book();
    updated.setId("uuid-123");
    updated.setTitle("Nouveau titre");
    updated.setDescription("Nouvelle description");

    when(bookRepository.findById("uuid-123")).thenReturn(Optional.of(existing));
    when(bookRepository.save(any(Book.class))).thenReturn(updated);

    Book result = bookService.updateBook("uuid-123", request);

    assertEquals("Nouveau titre", result.getTitle());
    assertEquals("Nouvelle description", result.getDescription());
    verify(bookRepository).findById("uuid-123");
    verify(bookRepository).save(any(Book.class));
  }

  @Test
  void testUpdateBook_notFound() {
    when(bookRepository.findById("unknown")).thenReturn(Optional.empty());

    BookRequest request = new BookRequest();
    request.setTitle("Peu importe");

    RuntimeException ex =
        assertThrows(RuntimeException.class, () -> bookService.updateBook("unknown", request));

    assertTrue(ex.getMessage().contains("Book not found"));
    verify(bookRepository, never()).save(any());
  }
}
