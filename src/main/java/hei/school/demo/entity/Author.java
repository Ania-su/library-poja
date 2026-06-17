package hei.school.demo.entity;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Author {
  private String id;
  private String firstName;
  private String lastName;
  private String biography;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private List<Book> books;
}
