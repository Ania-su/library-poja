package hei.school.demo.repository.model;

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
public class JAuthor {
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
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private List<JBook> books;
}
