package hei.school.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import hei.school.demo.endpoint.rest.controller.dto.BookCopyUpdate;
import hei.school.demo.entity.Book;
import hei.school.demo.entity.BookCopy;
import hei.school.demo.entity.enums.BookFormat;
import hei.school.demo.exception.NotFoundException;
import hei.school.demo.repository.BookCopyRepository;
import hei.school.demo.repository.BookRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class BookCopyServiceTest {

  @Mock private BookCopyRepository bookCopyRepository;
  @Mock private BookRepository bookRepository;

  @InjectMocks private BookCopyService bookCopyService;

  private BookCopy bookCopy;

  @BeforeEach
  void setUp() {
    List<BookCopy> copies = new ArrayList<>();
    Book book =
        new Book(
            "book-uuid-001",
            "Le petit prince",
            LocalDate.of(2003, 1, 2),
            "this is the description",
            copies,
            null,
            null);
    bookCopy = new BookCopy("bookcopy-uuid-1", book, BookFormat.POCKET, 10.0);
    copies.add(bookCopy);
  }

  @Test
  void findAll_shouldCallRepositoryWithSpecification() {
    // Given
    UUID bookId = UUID.randomUUID();
    BookFormat format = BookFormat.HARDBACK;
    Double minPrice = 10.00;
    double maxPrice = 20.00;
    List<BookCopy> expectedCopies = List.of(new BookCopy());

    when(bookCopyRepository.findAll(any(Specification.class))).thenReturn(expectedCopies);

    // When
    List<BookCopy> actualCopies = bookCopyService.findAll(bookId, format, minPrice, maxPrice);

    // Then
    assertEquals(expectedCopies, actualCopies);
    verify(bookCopyRepository).findAll(any(Specification.class));
  }

  @Test
  void findAll_withNullParams_shouldStillCallRepository() {
    // Given
    List<BookCopy> expectedCopies = List.of(new BookCopy());
    when(bookCopyRepository.findAll(any(Specification.class))).thenReturn(expectedCopies);

    // When
    List<BookCopy> actualCopies = bookCopyService.findAll(null, null, null, null);

    // Then
    assertEquals(expectedCopies, actualCopies);
    verify(bookCopyRepository).findAll(any(Specification.class));
  }

  @Test
  void save_shouldSaveBookCopy() {
    // Given
    String bookId = UUID.randomUUID().toString();
    BookCopy bookCopyRequest = new BookCopy();
    bookCopyRequest.setId(bookId);
    bookCopyRequest.setFormat(BookFormat.HARDBACK);
    bookCopyRequest.setSellingPrice(10.00);

    Book parentBook = new Book();
    parentBook.setId(bookId);

    when(bookRepository.findById(bookId)).thenReturn(java.util.Optional.of(parentBook));
    when(bookCopyRepository.save(any(BookCopy.class))).thenAnswer(i -> i.getArguments()[0]);

    // When
    BookCopy savedCopy = bookCopyService.save(bookCopyRequest);

    // Then
    assertEquals(parentBook, savedCopy.getBook());
    assertEquals(BookFormat.HARDBACK, savedCopy.getFormat());
    assertEquals(10.00, savedCopy.getSellingPrice());
    verify(bookCopyRepository).save(any(BookCopy.class));
  }

  @Test
  void save_withNegativePrice_shouldThrowException() {
    // Given
    BookCopy bookCopy = new BookCopy();
    bookCopy.setSellingPrice(-1.00);

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> bookCopyService.save(bookCopy));
  }

  @Test
  void updateBookCopy_shouldUpdateFormat_whenOnlyFormatProvided() {
    BookCopyUpdate patch = new BookCopyUpdate();
    patch.setFormat(BookFormat.HARDBACK);

    when(bookCopyRepository.findById("bookcopy-uuid-1")).thenReturn(Optional.of(bookCopy));
    when(bookCopyRepository.save(bookCopy)).thenReturn(bookCopy);

    BookCopy result = bookCopyService.updateBookCopy("bookcopy-uuid-1", patch);

    assertEquals(BookFormat.HARDBACK, result.getFormat());
    assertEquals(10.0, result.getSellingPrice());
    verify(bookCopyRepository).save(bookCopy);
  }

  @Test
  void updateBookCopy_shouldUpdateSellingPrice_whenOnlySellingPriceProvided() {
    BookCopyUpdate patch = new BookCopyUpdate();
    patch.setSellingPrice(25.0);

    when(bookCopyRepository.findById("bookcopy-uuid-1")).thenReturn(Optional.of(bookCopy));
    when(bookCopyRepository.save(bookCopy)).thenReturn(bookCopy);

    BookCopy result = bookCopyService.updateBookCopy("bookcopy-uuid-1", patch);

    assertEquals(25.0, result.getSellingPrice());
    assertEquals(BookFormat.POCKET, result.getFormat());
    verify(bookCopyRepository).save(bookCopy);
  }

  @Test
  void updateBookCopy_shouldThrowNotFoundException_whenIdDoesNotExist() {

    when(bookCopyRepository.findById("unexisting-uuid-1")).thenReturn(Optional.empty());

    NotFoundException ex =
        assertThrows(
            NotFoundException.class,
            () -> bookCopyService.updateBookCopy("unexisting-uuid-1", new BookCopyUpdate()));

    assertEquals("BookCopy not found with id : unexisting-uuid-1", ex.getMessage());
    verify(bookCopyRepository, never()).save(any());
  }

  @Test
  void deleteBookCopy_shouldReturnDeletedBookCopy_whenIdExists() {
    when(bookCopyRepository.findById("bookcopy-uuid-1")).thenReturn(Optional.of(bookCopy));

    BookCopy result = bookCopyService.deleteBookCopy("bookcopy-uuid-1");

    assertEquals(bookCopy, result);
    verify(bookCopyRepository).delete(bookCopy);
  }

  @Test
  void deleteBookCopy_shouldThrowNotFoundException_whenIdDoesNotExist() {
    when(bookCopyRepository.findById("uuid-notfound-1")).thenReturn(Optional.empty());

    NotFoundException ex =
        assertThrows(
            NotFoundException.class, () -> bookCopyService.deleteBookCopy("uuid-notfound-1"));

    assertEquals("BookCopy not found with id : uuid-notfound-1", ex.getMessage());
    verify(bookCopyRepository, never()).delete((BookCopy) any());
  }

  @Test
  void save_withNonExistentBook_shouldThrowException() {
    // Given
    String bookId = UUID.randomUUID().toString();
    BookCopy bookCopy = new BookCopy();
    bookCopy.setId(bookId);
    bookCopy.setSellingPrice(10.00);

    when(bookRepository.findById(bookId)).thenReturn(java.util.Optional.empty());

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> bookCopyService.save(bookCopy));
  }

  @Test
  void getCopyById_shouldReturnBookCopy() {
    // Given
    String id = "test-id";
    BookCopy expectedCopy = new BookCopy();
    when(bookCopyRepository.findById(id)).thenReturn(java.util.Optional.of(expectedCopy));

    // When
    BookCopy actualCopy = bookCopyService.getCopyById(id);

    // Then
    assertEquals(expectedCopy, actualCopy);
    verify(bookCopyRepository).findById(id);
  }

  @Test
  void getCopyById_withNonExistentId_shouldThrowException() {
    // Given
    String id = "non-existent-id";
    when(bookCopyRepository.findById(id)).thenReturn(java.util.Optional.empty());

    // When & Then
    assertThrows(RuntimeException.class, () -> bookCopyService.getCopyById(id));
  }
}
