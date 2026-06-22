package hei.school.demo.service;

import hei.school.demo.endpoint.rest.controller.dto.AuthorRequest;
import hei.school.demo.entity.Author;
import hei.school.demo.repository.AuthorRepository;
import hei.school.demo.repository.mapper.AuthorMapper;
import hei.school.demo.repository.model.JAuthor;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthorService {
  private final AuthorRepository authorRepository;
  private final AuthorMapper authorMapper;

  public List<Author> findAll() {
    List<JAuthor> jAuthors = authorRepository.findAll();
    return authorMapper.toDomain(jAuthors);
  }

  public Author createAuthor(AuthorRequest newAuthor) {
    Author author = new Author();
    author.setFirstName(newAuthor.getFirstName());
    author.setLastName(newAuthor.getLastName());
    author.setBiography(newAuthor.getBiography());
    JAuthor jAuthor = authorMapper.toJpa(author);
    JAuthor saved = authorRepository.save(jAuthor);
    return authorMapper.toDomain(saved);
  }

  public Author getAuthorById(UUID id) {
    JAuthor jAuthor =
        authorRepository
            .findById(id.toString())
            .orElseThrow(() -> new RuntimeException("Author not found"));
    return authorMapper.toDomain(jAuthor);
  }

  public Author updateAuthor(UUID id, AuthorRequest updateAuthor) {
    JAuthor existing =
        authorRepository
            .findById(id.toString())
            .orElseThrow(() -> new RuntimeException("Author not found"));
    existing.setFirstName(updateAuthor.getFirstName());
    existing.setLastName(updateAuthor.getLastName());
    existing.setBiography(updateAuthor.getBiography());
    JAuthor saved = authorRepository.save(existing);
    return authorMapper.toDomain(saved);
  }

  public void deleteAuthor(UUID id) {
    JAuthor jAuthor =
        authorRepository
            .findById(id.toString())
            .orElseThrow(() -> new RuntimeException("Author not found"));
    authorRepository.delete(jAuthor);
  }
}
