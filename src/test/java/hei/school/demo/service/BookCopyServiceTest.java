package hei.school.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import hei.school.demo.entity.Book;
import hei.school.demo.entity.BookCopy;
import hei.school.demo.entity.enums.BookFormat;
import hei.school.demo.repository.BookCopyRepository;
import hei.school.demo.repository.BookRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
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

  @Test
  void findAll_shouldCallRepositoryWithSpecification() {
    // Given
    UUID bookId = UUID.randomUUID();
    BookFormat format = BookFormat.HARDBACK;
    BigDecimal minPrice = new BigDecimal("10.00");
    BigDecimal maxPrice = new BigDecimal("20.00");
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
    bookCopyRequest.setSellingPrice(new BigDecimal("10.00"));

    Book parentBook = new Book();
    parentBook.setId(bookId);

    when(bookRepository.findById(bookId)).thenReturn(java.util.Optional.of(parentBook));
    when(bookCopyRepository.save(any(BookCopy.class))).thenAnswer(i -> i.getArguments()[0]);

    // When
    BookCopy savedCopy = bookCopyService.save(bookCopyRequest);

    // Then
    assertEquals(parentBook, savedCopy.getBook());
    assertEquals(BookFormat.HARDBACK, savedCopy.getFormat());
    assertEquals(new BigDecimal("10.00"), savedCopy.getSellingPrice());
    verify(bookCopyRepository).save(any(BookCopy.class));
  }

  @Test
  void save_withNegativePrice_shouldThrowException() {
    // Given
    BookCopy bookCopy = new BookCopy();
    bookCopy.setSellingPrice(new BigDecimal("-1.00"));

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> bookCopyService.save(bookCopy));
  }

  @Test
  void save_withNonExistentBook_shouldThrowException() {
    // Given
    String bookId = UUID.randomUUID().toString();
    BookCopy bookCopy = new BookCopy();
    bookCopy.setId(bookId);
    bookCopy.setSellingPrice(new BigDecimal("10.00"));

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

