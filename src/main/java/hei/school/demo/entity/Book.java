package hei.school.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "book")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Book {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Column(nullable = false)
  private String title;

  @Column(name = "publication_date")
  private LocalDate publicationDate;

  @Column(columnDefinition = "TEXT")
  private String description;

  @OneToMany(mappedBy = "book")
  private List<BookCopy> copies;
}
