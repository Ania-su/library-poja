package hei.school.demo.repository.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import hei.school.demo.entity.Author;
import hei.school.demo.entity.Book;
import hei.school.demo.entity.Genre;
import hei.school.demo.repository.model.JAuthor;
import hei.school.demo.repository.model.JBook;
import hei.school.demo.repository.model.JGenre;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookMapperTest {

  @Mock private AuthorMapper authorMapper;

  @Mock private GenreMapper genreMapper;

  @InjectMocks private BookMapper bookMapper;

  private JBook jBook;
  private UUID bookId;

  @BeforeEach
  void setUp() {
    bookId = UUID.randomUUID();
    jBook = new JBook();
    jBook.setId(bookId);
    jBook.setTitle("Le Petit Prince");
    jBook.setPublicationDate(LocalDate.of(1943, 4, 6));
    jBook.setDescription("Un conte poétique.");
  }

  @Test
  void toDomain_shouldMapAuthorsAndGenres_byDelegatingToTheirMappers() {
    var jAuthor = new JAuthor();
    jAuthor.setId("author-1");
    var jGenre = new JGenre();
    jGenre.setId("genre-1");
    jBook.setAuthors(List.of(jAuthor));
    jBook.setGenres(List.of(jGenre));

    var author = new Author();
    author.setId("author-1");
    var genre = new Genre();
    genre.setId("genre-1");
    when(authorMapper.toDomain(jAuthor)).thenReturn(author);
    when(genreMapper.toDomain(jGenre)).thenReturn(genre);

    var book = bookMapper.toDomain(jBook);

    assertThat(book.getId()).isEqualTo(bookId);
    assertThat(book.getTitle()).isEqualTo("Le Petit Prince");
    assertThat(book.getPublicationDate()).isEqualTo(LocalDate.of(1943, 4, 6));
    assertThat(book.getDescription()).isEqualTo("Un conte poétique.");
    assertThat(book.getAuthors()).containsExactly(author);
    assertThat(book.getGenres()).containsExactly(genre);
    verify(authorMapper).toDomain(jAuthor);
    verify(genreMapper).toDomain(jGenre);
  }

  @Test
  void toDomain_shouldKeepAuthorsNull_whenJBookHasNoAuthors() {
    var book = bookMapper.toDomain(jBook);

    assertThat(book.getAuthors()).isNull();
  }

  @Test
  void toDomain_shouldKeepGenresNull_whenJBookHasNoGenres() {
    var book = bookMapper.toDomain(jBook);

    assertThat(book.getGenres()).isNull();
  }

  @Test
  void toDomain_shouldReturnNull_whenJBookIsNull() {
    assertThat(bookMapper.toDomain((JBook) null)).isNull();
  }

  @Test
  void toDomain_shouldMapList() {
    var jBook2 = new JBook();
    jBook2.setId(UUID.randomUUID());

    var books = bookMapper.toDomain(List.of(jBook, jBook2));

    assertThat(books).hasSize(2);
    assertThat(books.get(0).getId()).isEqualTo(bookId);
    assertThat(books.get(1).getId()).isEqualTo(jBook2.getId());
  }

  @Test
  void toDomain_shouldReturnNull_whenListIsNull() {
    assertThat(bookMapper.toDomain((List<JBook>) null)).isNull();
  }

  @Test
  void toJpa_shouldMapScalarFields_butNotAuthorsOrGenres() {
    var book = new Book();
    book.setId(bookId);
    book.setTitle("Le Petit Prince");
    book.setPublicationDate(LocalDate.of(1943, 4, 6));
    book.setDescription("Un conte poétique.");
    book.setAuthors(List.of(new Author()));
    book.setGenres(List.of(new Genre()));

    var mappedJBook = bookMapper.toJpa(book);

    assertThat(mappedJBook.getId()).isEqualTo(bookId);
    assertThat(mappedJBook.getTitle()).isEqualTo("Le Petit Prince");
    assertThat(mappedJBook.getPublicationDate()).isEqualTo(LocalDate.of(1943, 4, 6));
    assertThat(mappedJBook.getDescription()).isEqualTo("Un conte poétique.");
    assertThat(mappedJBook.getAuthors()).isNull();
    assertThat(mappedJBook.getGenres()).isNull();
  }

  @Test
  void toJpa_shouldReturnNull_whenBookIsNull() {
    assertThat(bookMapper.toJpa(null)).isNull();
  }
}
