package hei.school.demo.repository.mapper;

import hei.school.demo.entity.Author;
import hei.school.demo.repository.model.JAuthor;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {

  public Author toDomain(JAuthor jAuthor) {
    if (jAuthor == null) return null;
    Author author = new Author();
    author.setId(jAuthor.getId());
    author.setFirstName(jAuthor.getFirstName());
    author.setLastName(jAuthor.getLastName());
    author.setBiography(jAuthor.getBiography());
    return author;
  }

  public List<Author> toDomain(List<JAuthor> jAuthors) {
    return jAuthors.stream().map(this::toDomain).toList();
  }

  public JAuthor toJpa(Author author) {
    if (author == null) return null;
    JAuthor jAuthor = new JAuthor();
    jAuthor.setId(author.getId());
    jAuthor.setFirstName(author.getFirstName());
    jAuthor.setLastName(author.getLastName());
    jAuthor.setBiography(author.getBiography());
    return jAuthor;
  }
}
