package hei.school.demo.repository.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import hei.school.demo.entity.Genre;
import hei.school.demo.repository.model.JGenre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GenreMapperTest {

  private GenreMapper genreMapper;

  @BeforeEach
  void setUp() {
    genreMapper = new GenreMapper();
  }

  @Test
  void toDomain_shouldMapAllFields() {
    var jGenre = new JGenre();
    jGenre.setId("genre-1");
    jGenre.setName("Fantasy");
    jGenre.setDescription("Univers imaginaires.");

    var genre = genreMapper.toDomain(jGenre);

    assertThat(genre.getId()).isEqualTo("genre-1");
    assertThat(genre.getName()).isEqualTo("Fantasy");
    assertThat(genre.getDescription()).isEqualTo("Univers imaginaires.");
  }

  @Test
  void toDomain_shouldReturnNull_whenInputIsNull() {
    assertThat(genreMapper.toDomain(null)).isNull();
  }

  @Test
  void toJpa_shouldMapAllFields() {
    var genre = new Genre();
    genre.setId("genre-1");
    genre.setName("Fantasy");
    genre.setDescription("Univers imaginaires.");

    var jGenre = genreMapper.toJpa(genre);

    assertThat(jGenre.getId()).isEqualTo("genre-1");
    assertThat(jGenre.getName()).isEqualTo("Fantasy");
    assertThat(jGenre.getDescription()).isEqualTo("Univers imaginaires.");
  }

  @Test
  void toJpa_shouldReturnNull_whenInputIsNull() {
    assertThat(genreMapper.toJpa(null)).isNull();
  }
}
