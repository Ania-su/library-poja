package hei.school.demo.repository.mapper;

import hei.school.demo.entity.Genre;
import hei.school.demo.repository.model.JGenre;
import org.springframework.stereotype.Component;

@Component
public class GenreMapper {

  public Genre toDomain(JGenre jGenre) {
    if (jGenre == null) return null;
    Genre genre = new Genre();
    genre.setId(jGenre.getId());
    genre.setName(jGenre.getName());
    genre.setDescription(jGenre.getDescription());
    return genre;
  }

  public JGenre toJpa(Genre genre) {
    if (genre == null) return null;
    JGenre jGenre = new JGenre();
    jGenre.setId(genre.getId());
    jGenre.setName(genre.getName());
    jGenre.setDescription(genre.getDescription());
    return jGenre;
  }
}
