package hei.school.demo.entity;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Author {
  private String id;
  private String firstName;
  private String lastName;
  private String nationality;
  private String biography;
  private List<BookAuthor> bookAuthors;
}
