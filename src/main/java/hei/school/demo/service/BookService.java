package hei.school.demo.service;

import hei.school.demo.endpoint.rest.controller.dto.BookRequest;
import hei.school.demo.entity.Book;
import hei.school.demo.repository.BookRepository;
import hei.school.demo.repository.mapper.BookMapper;
import hei.school.demo.repository.model.JAuthor;
import hei.school.demo.repository.model.JBook;
import hei.school.demo.repository.model.JGenre;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BookService {

  private final BookRepository bookRepository;
  private final BookMapper bookMapper;

  public List<Book> getBooks(
      String title,
      String description,
      LocalDate before,
      List<String> authorIds,
      List<String> genreIds,
      LocalDate after,
      int page,
      int perPage) {

    Pageable pageable = PageRequest.of(page - 1, perPage);
    List<JBook> jBooks =
        bookRepository
            .findAll(buildSpec(title, description, authorIds, genreIds, before, after), pageable)
            .getContent();
    return bookMapper.toDomain(jBooks);
  }

  public long countBooks(
      String title,
      String description,
      List<String> authorIds,
      List<String> genreIds,
      LocalDate before,
      LocalDate after) {

    return bookRepository.count(buildSpec(title, description, authorIds, genreIds, before, after));
  }

  private Specification<JBook> buildSpec(
      String title,
      String description,
      List<String> authorIds,
      List<String> genreIds,
      LocalDate before,
      LocalDate after) {

    return (root, query, cb) -> {
      ArrayList<Predicate> predicates = new ArrayList<>();

      if (title != null && !title.isEmpty()) {
        predicates.add(cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
      }

      if (description != null && !description.isEmpty()) {
        predicates.add(
            cb.like(cb.lower(root.get("description")), "%" + description.toLowerCase() + "%"));
      }

      if (authorIds != null && !authorIds.isEmpty()) {
        Join<JBook, JAuthor> authorJoin = root.join("authors");
        predicates.add(authorJoin.get("id").in(authorIds));
      }

      if (genreIds != null && !genreIds.isEmpty()) {
        Join<JBook, JGenre> genreJoin = root.join("genres");
        predicates.add(genreJoin.get("id").in(genreIds));
      }

      if (before != null) {
        predicates.add(cb.lessThanOrEqualTo(root.get("publicationDate"), before));
      }

      if (after != null) {
        predicates.add(cb.greaterThanOrEqualTo(root.get("publicationDate"), after));
      }

      if (!predicates.isEmpty()) {
        query.distinct(true);
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }

  public Book createBook(BookRequest book) {
    Book toSave = new Book();
    toSave.setTitle(book.getTitle());
    toSave.setDescription(book.getDescription());
    toSave.setPublicationDate(book.getPublicationDate());
    JBook jBook = bookMapper.toJpa(toSave);
    JBook saved = bookRepository.save(jBook);
    return bookMapper.toDomain(saved);
  }

  public Book getBookById(UUID id) {
    JBook jBook =
        bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
    return bookMapper.toDomain(jBook);
  }

  public Book updateBook(UUID id, BookRequest book) {
    JBook existing =
        bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
    existing.setTitle(book.getTitle());
    existing.setDescription(book.getDescription());
    existing.setPublicationDate(book.getPublicationDate());
    JBook saved = bookRepository.save(existing);
    return bookMapper.toDomain(saved);
  }

  public Book deleteBook(UUID id) {
    JBook jBook =
        bookRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
    bookRepository.deleteById(id);
    return bookMapper.toDomain(jBook);
  }
}
