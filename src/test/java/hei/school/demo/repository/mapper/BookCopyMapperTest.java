package hei.school.demo.repository.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import hei.school.demo.entity.Book;
import hei.school.demo.entity.BookCopy;
import hei.school.demo.entity.enums.BookFormat;
import hei.school.demo.repository.model.JBook;
import hei.school.demo.repository.model.JBookCopy;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BookCopyMapperTest {

  private BookCopyMapper bookCopyMapper;

  @BeforeEach
  void setUp() {
    bookCopyMapper = new BookCopyMapper();
  }

  @Test
  void toDomain_shouldMapAllFields_withBook() {
    var copyId = UUID.randomUUID();
    var bookId = UUID.randomUUID();
    var jBook = new JBook();
    jBook.setId(bookId);
    jBook.setTitle("Le Petit Prince");
    jBook.setPublicationDate(LocalDate.of(1943, 4, 6));
    jBook.setDescription("Un conte poétique.");

    var jBookCopy = new JBookCopy();
    jBookCopy.setId(copyId);
    jBookCopy.setFormat(BookFormat.HARDBACK);
    jBookCopy.setSellingPrice(19.99);
    jBookCopy.setBook(jBook);

    var bookCopy = bookCopyMapper.toDomain(jBookCopy);

    assertThat(bookCopy.getId()).isEqualTo(copyId);
    assertThat(bookCopy.getFormat()).isEqualTo(BookFormat.HARDBACK);
    assertThat(bookCopy.getSellingPrice()).isEqualTo(19.99);
    assertThat(bookCopy.getBook().getId()).isEqualTo(bookId);
    assertThat(bookCopy.getBook().getTitle()).isEqualTo("Le Petit Prince");
    assertThat(bookCopy.getBook().getPublicationDate()).isEqualTo(LocalDate.of(1943, 4, 6));
    assertThat(bookCopy.getBook().getDescription()).isEqualTo("Un conte poétique.");
  }

  @Test
  void toDomain_shouldKeepBookNull_whenJBookCopyHasNoBook() {
    var jBookCopy = new JBookCopy();
    jBookCopy.setId(UUID.randomUUID());
    jBookCopy.setFormat(BookFormat.HARDBACK);
    jBookCopy.setSellingPrice(9.99);

    var bookCopy = bookCopyMapper.toDomain(jBookCopy);

    assertThat(bookCopy.getBook()).isNull();
  }

  @Test
  void toDomain_shouldReturnNull_whenJBookCopyIsNull() {
    assertThat(bookCopyMapper.toDomain((JBookCopy) null)).isNull();
  }

  @Test
  void toDomain_shouldMapList() {
    var jBookCopy1 = new JBookCopy();
    jBookCopy1.setId(UUID.randomUUID());
    var jBookCopy2 = new JBookCopy();
    jBookCopy2.setId(UUID.randomUUID());

    var bookCopies = bookCopyMapper.toDomain(List.of(jBookCopy1, jBookCopy2));

    assertThat(bookCopies).hasSize(2);
    assertThat(bookCopies.get(0).getId()).isEqualTo(jBookCopy1.getId());
    assertThat(bookCopies.get(1).getId()).isEqualTo(jBookCopy2.getId());
  }

  @Test
  void toDomain_shouldReturnNull_whenListIsNull() {
    assertThat(bookCopyMapper.toDomain((List<JBookCopy>) null)).isNull();
  }

  @Test
  void toJpa_shouldMapAllFields_withBookIdOnly() {
    var copyId = UUID.randomUUID();
    var bookId = UUID.randomUUID();
    var book = new Book();
    book.setId(bookId);
    var bookCopy = new BookCopy();
    bookCopy.setId(copyId);
    bookCopy.setFormat(BookFormat.PAPERBACK);
    bookCopy.setSellingPrice(12.5);
    bookCopy.setBook(book);

    var jBookCopy = bookCopyMapper.toJpa(bookCopy);

    assertThat(jBookCopy.getId()).isEqualTo(copyId);
    assertThat(jBookCopy.getFormat()).isEqualTo(BookFormat.PAPERBACK);
    assertThat(jBookCopy.getSellingPrice()).isEqualTo(12.5);
    assertThat(jBookCopy.getBook().getId()).isEqualTo(bookId);
  }

  @Test
  void toJpa_shouldKeepBookNull_whenBookCopyHasNoBook() {
    var bookCopy = new BookCopy();
    bookCopy.setId(UUID.randomUUID());
    bookCopy.setFormat(BookFormat.PAPERBACK);
    bookCopy.setSellingPrice(12.5);

    var jBookCopy = bookCopyMapper.toJpa(bookCopy);

    assertThat(jBookCopy.getBook()).isNull();
  }

  @Test
  void toJpa_shouldReturnNull_whenBookCopyIsNull() {
    assertThat(bookCopyMapper.toJpa(null)).isNull();
  }
}
