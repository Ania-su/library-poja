package hei.school.demo.entity;

import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "publisher")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Publisher {
  @Id private String id;

  @Column(nullable = false)
  private String name;

  private String country;
  private String website;

  @OneToMany(mappedBy = "publisher")
  private List<Book> books;
}
