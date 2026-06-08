package hei.school.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "author")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Author {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  //  private String nationality;

  @Column(columnDefinition = "TEXT")
  private String biography;

  //  @OneToMany(mappedBy = "author")
  //  private List<BookAuthor> bookAuthors;
}
