package hei.school.demo.repository.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import hei.school.demo.repository.model.JAuthor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AuthorMapperTest {

  private AuthorMapper authorMapper;

  @BeforeEach
  void setUp() {
    authorMapper = new AuthorMapper();
  }

  @Test
  void toDomain_shouldMapAllFields() {
    var jAuthor = new JAuthor();
    jAuthor.setId("author-1");
    jAuthor.setFirstName("Jean");
    jAuthor.setLastName("Rakoto");
    jAuthor.setBiography("Auteur malgache.");

    var author = authorMapper.toDomain(jAuthor);

    assertThat(author.getId()).isEqualTo("author-1");
    assertThat(author.getFirstName()).isEqualTo("Jean");
    assertThat(author.getLastName()).isEqualTo("Rakoto");
    assertThat(author.getBiography()).isEqualTo("Auteur malgache.");
  }

  @Test
  void toDomain_shouldReturnNull_whenInputIsNull() {
    assertThat(authorMapper.toDomain(null)).isNull();
  }

  @Test
  void toJpa_shouldMapAllFields() {
    var author = new hei.school.demo.entity.Author();
    author.setId("author-1");
    author.setFirstName("Jean");
    author.setLastName("Rakoto");
    author.setBiography("Auteur malgache.");

    var jAuthor = authorMapper.toJpa(author);

    assertThat(jAuthor.getId()).isEqualTo("author-1");
    assertThat(jAuthor.getFirstName()).isEqualTo("Jean");
    assertThat(jAuthor.getLastName()).isEqualTo("Rakoto");
    assertThat(jAuthor.getBiography()).isEqualTo("Auteur malgache.");
  }

  @Test
  void toJpa_shouldReturnNull_whenInputIsNull() {
    assertThat(authorMapper.toJpa(null)).isNull();
  }
}
