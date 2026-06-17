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
public class Genre {
  private String id;
  private String name;
  private String description;

  @ToString.Exclude @EqualsAndHashCode.Exclude private List<Book> books;
}
