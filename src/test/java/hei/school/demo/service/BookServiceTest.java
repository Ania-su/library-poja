package hei.school.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import hei.school.demo.endpoint.rest.controller.dto.BookRequest;
import hei.school.demo.entity.Book;
import hei.school.demo.repository.BookRepository;
import hei.school.demo.repository.mapper.AuthorMapper;
import hei.school.demo.repository.mapper.BookMapper;
import hei.school.demo.repository.mapper.GenreMapper;
import hei.school.demo.repository.model.JBook;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
  @Mock private BookRepository bookRepository;

  private BookService bookService;

  @BeforeEach
  void setUp() {
    BookMapper bookMapper = new BookMapper(new AuthorMapper(), new GenreMapper());
    bookService = new BookService(bookRepository, bookMapper);
  }

  @Test
  void getBooks_shouldReturnAllBooks_whenNoFilterProvided() {
    JBook jBook1 = new JBook();
    jBook1.setId(UUID.fromString("00000000-0000-0000-0000-111111111112"));
    jBook1.setTitle("Méthode Boscher");
    jBook1.setPublicationDate(LocalDate.of(2008, 8, 1));
    jBook1.setDescription("Livre éducatif pour apprendre le français");

    when(bookRepository.findAll(any(Specification.class), eq(PageRequest.of(0, 10))))
        .thenReturn(new PageImpl<>(List.of(jBook1)));

    List<Book> result = bookService.getBooks(null, null, null, null, null, null, 1, 10);

    assertEquals(1, result.size());
    assertEquals("Méthode Boscher", result.get(0).getTitle());
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
    JBook jBook2 = new JBook();
    jBook2.setId(UUID.fromString("00000000-0000-0000-0000-111111111117"));
    jBook2.setTitle("Atomic Habits");

    when(bookRepository.findAll(any(Specification.class), eq(PageRequest.of(1, 5))))
        .thenReturn(new PageImpl<>(List.of(jBook2)));

    List<Book> result = bookService.getBooks(null, null, null, null, null, null, 2, 5);

    assertEquals(1, result.size());
    assertEquals("Atomic Habits", result.get(0).getTitle());
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

    JBook savedJBook = new JBook();
    UUID generatedId = UUID.fromString("00000000-0000-0000-0000-111111111118");
    savedJBook.setId(generatedId);
    savedJBook.setTitle("Le Petit Prince");
    savedJBook.setDescription("Un classique");
    savedJBook.setPublicationDate(LocalDate.of(1943, 4, 6));

    when(bookRepository.save(any(JBook.class))).thenReturn(savedJBook);

    Book result = bookService.createBook(request);

    assertNotNull(result);
    assertEquals(generatedId, result.getId());
    assertEquals("Le Petit Prince", result.getTitle());
    assertEquals("Un classique", result.getDescription());
    verify(bookRepository, times(1)).save(any(JBook.class));
  }

  @Test
  void testGetBookById() {
    JBook jBook = new JBook();
    UUID bookId = UUID.fromString("00000000-0000-0000-0000-000000000123");
    jBook.setId(bookId);
    jBook.setTitle("1984");

    when(bookRepository.findById(bookId)).thenReturn(Optional.of(jBook));

    Book result = bookService.getBookById(bookId);

    assertNotNull(result);
    assertEquals(bookId, result.getId());
    assertEquals("1984", result.getTitle());
    verify(bookRepository, times(1)).findById(bookId);
  }

  @Test
  void testGetBookById_notFound() {
    UUID unknownId = UUID.fromString("00000000-0000-0000-0000-999999999999");
    when(bookRepository.findById(unknownId)).thenReturn(Optional.empty());

    RuntimeException ex =
        assertThrows(RuntimeException.class, () -> bookService.getBookById(unknownId));

    assertTrue(ex.getMessage().contains("Book not found"));
  }

  @Test
  void testUpdateBook() {
    JBook existingJBook = new JBook();
    UUID updateBookId = UUID.fromString("00000000-0000-0000-0000-000000001234");
    existingJBook.setId(updateBookId);
    existingJBook.setTitle("Ancien titre");

    BookRequest request = new BookRequest();
    request.setTitle("Nouveau titre");
    request.setDescription("Nouvelle description");
    request.setPublicationDate(LocalDate.of(2000, 1, 1));

    JBook updatedJBook = new JBook();
    updatedJBook.setId(updateBookId);
    updatedJBook.setTitle("Nouveau titre");
    updatedJBook.setDescription("Nouvelle description");
    updatedJBook.setPublicationDate(LocalDate.of(2000, 1, 1));

    when(bookRepository.findById(updateBookId)).thenReturn(Optional.of(existingJBook));
    when(bookRepository.save(any(JBook.class))).thenReturn(updatedJBook);

    Book result = bookService.updateBook(updateBookId, request);

    assertEquals("Nouveau titre", result.getTitle());
    assertEquals("Nouvelle description", result.getDescription());
    verify(bookRepository).findById(updateBookId);
    verify(bookRepository).save(any(JBook.class));
  }

  @Test
  void testUpdateBook_notFound() {
    UUID unknownId = UUID.fromString("00000000-0000-0000-0000-999999999998");
    when(bookRepository.findById(unknownId)).thenReturn(Optional.empty());

    BookRequest request = new BookRequest();
    request.setTitle("Peu importe");

    RuntimeException ex =
        assertThrows(RuntimeException.class, () -> bookService.updateBook(unknownId, request));

    assertTrue(ex.getMessage().contains("Book not found"));
    verify(bookRepository, never()).save(any());
  }

  @Test
  void testDeleteBook() {
    UUID bookId = UUID.fromString("00000000-0000-0000-0000-000000000456");
    var jBook = new JBook();
    jBook.setId(bookId);
    jBook.setTitle("book to delete");

    when(bookRepository.findById(bookId)).thenReturn(Optional.of(jBook));

    var result = bookService.deleteBook(bookId);

    assertNotNull(result);
    assertEquals(bookId, result.getId());
    assertEquals("book to delete", result.getTitle());
    verify(bookRepository).deleteById(bookId);
  }

  @Test
  void testDeleteBook_notFound() {
    var unknownId = UUID.fromString("00000000-0000-0000-0000-999999999997");
    when(bookRepository.findById(unknownId)).thenReturn(Optional.empty());

    var ex = assertThrows(RuntimeException.class, () -> bookService.deleteBook(unknownId));

    assertTrue(ex.getMessage().contains("Book not found"));
    verify(bookRepository, never()).deleteById(any());
  }
}
