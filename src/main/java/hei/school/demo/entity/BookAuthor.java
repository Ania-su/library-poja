package hei.school.demo.entity;

import hei.school.demo.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookAuthor {
  private String id;
  private Book book;
  private Author author;
  private Role role;
}
