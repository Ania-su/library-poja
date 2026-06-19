package hei.school.demo.repository.mapper;

import hei.school.demo.entity.Author;
import hei.school.demo.entity.Book;
import hei.school.demo.entity.Genre;
import hei.school.demo.repository.model.JAuthor;
import hei.school.demo.repository.model.JBook;
import hei.school.demo.repository.model.JGenre;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookMapper {

  private final AuthorMapper authorMapper;
  private final GenreMapper genreMapper;

  public Book toDomain(JBook jBook) {
    if (jBook == null) return null;
    Book book = new Book();
    book.setId(jBook.getId());
    book.setTitle(jBook.getTitle());
    book.setPublicationDate(jBook.getPublicationDate());
    book.setDescription(jBook.getDescription());
    if (jBook.getAuthors() != null) {
      List<Author> authors = new ArrayList<>();
      for (JAuthor ja : jBook.getAuthors()) {
        authors.add(authorMapper.toDomain(ja));
      }
      book.setAuthors(authors);
    }
    if (jBook.getGenres() != null) {
      List<Genre> genres = new ArrayList<>();
      for (JGenre jg : jBook.getGenres()) {
        genres.add(genreMapper.toDomain(jg));
      }
      book.setGenres(genres);
    }
    return book;
  }

  public List<Book> toDomain(List<JBook> jBooks) {
    if (jBooks == null) return null;
    List<Book> books = new ArrayList<>();
    for (JBook jb : jBooks) {
      books.add(toDomain(jb));
    }
    return books;
  }

  public JBook toJpa(Book book) {
    if (book == null) return null;
    JBook jBook = new JBook();
    jBook.setId(book.getId());
    jBook.setTitle(book.getTitle());
    jBook.setPublicationDate(book.getPublicationDate());
    jBook.setDescription(book.getDescription());
    return jBook;
  }
}
