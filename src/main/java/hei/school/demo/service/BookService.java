package hei.school.demo.service;

import hei.school.demo.entity.Author;
import hei.school.demo.entity.Book;
import hei.school.demo.entity.Genre;
import hei.school.demo.repository.BookRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BookService {

  private final BookRepository bookRepository;

  public List<Book> getBooks(
      String title,
      String description,
      LocalDate before,
      String authorId,
      String genreId,
      LocalDate after,
      int page,
      int perPage) {

    Pageable pageable = PageRequest.of(page - 1, perPage);
    return bookRepository
        .findAll(buildSpec(title, description, authorId, genreId, before, after), pageable)
        .getContent();
  }

  public long countBooks(
      String title,
      String description,
      String authorId,
      String genreId,
      LocalDate before,
      LocalDate after) {

    return bookRepository.count(buildSpec(title, description, authorId, genreId, before, after));
  }

  private Specification<Book> buildSpec(
      String title,
      String description,
      String authorId,
      String genreId,
      LocalDate before,
      LocalDate after) {

    return (root, query, cb) -> {
      ArrayList<Predicate> predicates = new ArrayList<Predicate>();

      if (title != null && !title.isEmpty()) {
        predicates.add(cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
      }

      if (description != null && !description.isEmpty()) {
        predicates.add(
            cb.like(cb.lower(root.get("description")), "%" + description.toLowerCase() + "%"));
      }

      if (authorId != null && !authorId.isEmpty()) {
        Join<Book, Author> authorJoin = root.join("authors");
        predicates.add(cb.equal(authorJoin.get("id"), authorId));
      }

      if (genreId != null && !genreId.isEmpty()) {
        Join<Book, Genre> genreJoin = root.join("genres");
        predicates.add(cb.equal(genreJoin.get("id"), genreId));
      }

      if (before != null) {
        predicates.add(cb.lessThan(root.get("publicationDate"), before));
      }

      if (after != null) {
        predicates.add(cb.greaterThan(root.get("publicationDate"), after));
      }

      if (!predicates.isEmpty()) {
        query.distinct(true);
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }
}
