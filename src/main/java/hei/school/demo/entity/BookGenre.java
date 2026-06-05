package hei.school.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookGenre {
  private String id;
  private Book book;
  private Genre genre;
}
