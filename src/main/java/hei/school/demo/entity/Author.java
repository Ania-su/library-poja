package hei.school.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "author")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Author {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Column(columnDefinition = "TEXT")
  private String biography;

  @ManyToMany(mappedBy = "authors")
  @JsonIgnore
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private List<Book> books;
}
