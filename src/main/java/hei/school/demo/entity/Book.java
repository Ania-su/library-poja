package hei.school.demo.entity;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Book {
  private String id;
  private String title;
  private LocalDate publicationDate;
  private String description;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private List<BookCopy> copies;

  private List<Genre> genres;
  private List<Author> authors;
}
