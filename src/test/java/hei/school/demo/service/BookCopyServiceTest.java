package hei.school.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import hei.school.demo.endpoint.rest.controller.dto.BookCopyUpdate;
import hei.school.demo.entity.BookCopy;
import hei.school.demo.entity.enums.BookFormat;
import hei.school.demo.exception.NotFoundException;
import hei.school.demo.repository.BookCopyRepository;
import hei.school.demo.repository.BookRepository;
import hei.school.demo.repository.mapper.BookCopyMapper;
import hei.school.demo.repository.model.JBook;
import hei.school.demo.repository.model.JBookCopy;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class BookCopyServiceTest {

  @Mock private BookCopyRepository bookCopyRepository;
  @Mock private BookRepository bookRepository;

  private BookCopyService bookCopyService;

  private JBookCopy jBookCopy;

  @BeforeEach
  void setUp() {
    bookCopyService = new BookCopyService(bookCopyRepository, bookRepository, new
BookCopyMapper());

    List<JBookCopy> copies = new ArrayList<>();
    JBook jBook = new JBook();
    jBook.setId(UUID.fromString("00000000-0000-0000-0000-111111111112"));
    jBook.setTitle("Le petit prince");
    jBook.setPublicationDate(LocalDate.of(2003, 1, 2));
    jBook.setDescription("this is the description");

    jBookCopy = new JBookCopy();
    jBookCopy.setId(UUID.fromString("00000000-0000-0000-0000-111111111111"));
    jBookCopy.setBook(jBook);
    jBookCopy.setFormat(BookFormat.POCKET);
    jBookCopy.setSellingPrice(10.0);

    copies.add(jBookCopy);
  }

  @Test
  void findAll_shouldCallRepositoryWithSpecification() {
    UUID bookId = UUID.randomUUID();
    BookFormat format = BookFormat.HARDBACK;
    Double minPrice = 10.00;
    double maxPrice = 20.00;

    JBookCopy expectedJCopy = new JBookCopy();
    expectedJCopy.setFormat(BookFormat.HARDBACK);
    expectedJCopy.setSellingPrice(15.0);


when(bookCopyRepository.findAll(any(Specification.class))).thenReturn(List.of(expectedJCopy));

    List<BookCopy> actualCopies = bookCopyService.findAll(bookId, format, minPrice, maxPrice);

    assertEquals(1, actualCopies.size());
    assertEquals(BookFormat.HARDBACK, actualCopies.get(0).getFormat());
    assertEquals(15.0, actualCopies.get(0).getSellingPrice());
    verify(bookCopyRepository).findAll(any(Specification.class));
  }

  @Test
  void findAll_withNullParams_shouldStillCallRepository() {
    JBookCopy expectedJCopy = new JBookCopy();

when(bookCopyRepository.findAll(any(Specification.class))).thenReturn(List.of(expectedJCopy));

    List<BookCopy> actualCopies = bookCopyService.findAll(null, null, null, null);

    assertEquals(1, actualCopies.size());
    verify(bookCopyRepository).findAll(any(Specification.class));
  }

  @Test
  void save_shouldSaveBookCopy() {
    UUID bookId = UUID.randomUUID();
    BookCopy bookCopyRequest = new BookCopy();
    bookCopyRequest.setId(bookId);
    bookCopyRequest.setFormat(BookFormat.HARDBACK);
    bookCopyRequest.setSellingPrice(10.00);

    JBook parentJBook = new JBook();
    parentJBook.setId(bookId);

    when(bookRepository.findById(bookId)).thenReturn(Optional.of(parentJBook));
    when(bookCopyRepository.save(any(JBookCopy.class))).thenAnswer(i -> i.getArguments()[0]);

    BookCopy savedCopy = bookCopyService.save(bookCopyRequest);

    assertEquals(bookId, savedCopy.getBook().getId());
    assertEquals(BookFormat.HARDBACK, savedCopy.getFormat());
    assertEquals(10.00, savedCopy.getSellingPrice());
    verify(bookCopyRepository).save(any(JBookCopy.class));
  }

  @Test
  void save_withNegativePrice_shouldThrowException() {
    BookCopy bookCopy = new BookCopy();
    bookCopy.setSellingPrice(-1.00);

    assertThrows(IllegalArgumentException.class, () -> bookCopyService.save(bookCopy));
  }

  @Test
  void updateBookCopy_shouldUpdateFormat_whenOnlyFormatProvided() {
    BookCopyUpdate patch = new BookCopyUpdate();
    patch.setFormat(BookFormat.HARDBACK);

    UUID bookCopyId = jBookCopy.getId();
    when(bookCopyRepository.findById(bookCopyId)).thenReturn(Optional.of(jBookCopy));
    when(bookCopyRepository.save(any(JBookCopy.class))).thenAnswer(i -> i.getArguments()[0]);

    BookCopy result = bookCopyService.updateBookCopy(bookCopyId, patch);

    assertEquals(BookFormat.HARDBACK, result.getFormat());
    assertEquals(10.0, result.getSellingPrice());
    verify(bookCopyRepository).save(any(JBookCopy.class));
  }

  @Test
  void updateBookCopy_shouldUpdateSellingPrice_whenOnlySellingPriceProvided() {
    BookCopyUpdate patch = new BookCopyUpdate();
    patch.setSellingPrice(25.0);

    UUID bookCopyId = jBookCopy.getId();
    when(bookCopyRepository.findById(bookCopyId)).thenReturn(Optional.of(jBookCopy));
    when(bookCopyRepository.save(any(JBookCopy.class))).thenAnswer(i -> i.getArguments()[0]);

    BookCopy result = bookCopyService.updateBookCopy(bookCopyId, patch);

    assertEquals(25.0, result.getSellingPrice());
    assertEquals(BookFormat.POCKET, result.getFormat());
    verify(bookCopyRepository).save(any(JBookCopy.class));
  }

  @Test
  void updateBookCopy_shouldThrowNotFoundException_whenIdDoesNotExist() {
    UUID unexistingId = UUID.fromString("00000000-0000-0000-0000-111111111113");
    when(bookCopyRepository.findById(unexistingId)).thenReturn(Optional.empty());

    NotFoundException ex =
        assertThrows(
            NotFoundException.class,
            () -> bookCopyService.updateBookCopy(unexistingId, new BookCopyUpdate()));

    assertEquals("BookCopy not found with id : " + unexistingId, ex.getMessage());
    verify(bookCopyRepository, never()).save(any());
  }

  @Test
  void deleteBookCopy_shouldReturnDeletedBookCopy_whenIdExists() {
    UUID bookCopyId = jBookCopy.getId();
    when(bookCopyRepository.findById(bookCopyId)).thenReturn(Optional.of(jBookCopy));

    BookCopy result = bookCopyService.deleteBookCopy(bookCopyId);

    assertEquals(bookCopyId, result.getId());
    verify(bookCopyRepository).delete(jBookCopy);
  }

  @Test
  void deleteBookCopy_shouldThrowNotFoundException_whenIdDoesNotExist() {
    UUID notFoundId = UUID.fromString("00000000-0000-0000-0000-111111111114");
    when(bookCopyRepository.findById(notFoundId)).thenReturn(Optional.empty());

    NotFoundException ex =
        assertThrows(
            NotFoundException.class, () -> bookCopyService.deleteBookCopy(notFoundId));

    assertEquals("BookCopy not found with id : " + notFoundId, ex.getMessage());
    verify(bookCopyRepository, never()).delete((JBookCopy) any());
  }

  @Test
  void save_withNonExistentBook_shouldThrowException() {
    UUID bookId = UUID.randomUUID();
    BookCopy bookCopy = new BookCopy();
    bookCopy.setId(bookId);
    bookCopy.setSellingPrice(10.00);

    when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

    assertThrows(IllegalArgumentException.class, () -> bookCopyService.save(bookCopy));
  }

  @Test
  void getCopyById_shouldReturnBookCopy() {
    UUID id = UUID.fromString("00000000-0000-0000-0000-111111111115");
    JBookCopy expectedJCopy = new JBookCopy();
    expectedJCopy.setId(id);
    expectedJCopy.setSellingPrice(9.99);

    when(bookCopyRepository.findById(id)).thenReturn(Optional.of(expectedJCopy));

    BookCopy actualCopy = bookCopyService.getCopyById(id);

    assertEquals(id, actualCopy.getId());
    assertEquals(9.99, actualCopy.getSellingPrice());
    verify(bookCopyRepository).findById(id);
  }

  @Test
  void getCopyById_withNonExistentId_shouldThrowException() {
    UUID id = UUID.fromString("00000000-0000-0000-0000-111111111116");
    when(bookCopyRepository.findById(id)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> bookCopyService.getCopyById(id));
  }
}
