package hei.school.demo.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import hei.school.demo.entity.BookCopy;
import hei.school.demo.entity.enums.BookFormat;
import hei.school.demo.repository.BookCopyRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookCopyServiceTest {

  @Mock
  private BookCopyRepository bookCopyRepository;

  @InjectMocks
  private BookCopyService bookCopyService;

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
}
