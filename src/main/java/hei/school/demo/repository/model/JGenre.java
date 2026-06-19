package hei.school.demo.repository.model;

import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "genre")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JGenre {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Column(nullable = false, unique = true)
  private String name;

  private String description;

  @ManyToMany(mappedBy = "genres")
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private List<JBook> books;
}
