package hei.school.demo.repository.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "book")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JBook {
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
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private List<JBookCopy> copies;

  @ManyToMany
  @JoinTable(
      name = "book_genre",
      joinColumns = @JoinColumn(name = "book_id"),
      inverseJoinColumns = @JoinColumn(name = "genre_id"))
  private List<JGenre> genres;

  @ManyToMany
  @JoinTable(
      name = "book_author",
      joinColumns = @JoinColumn(name = "book_id"),
      inverseJoinColumns = @JoinColumn(name = "author_id"))
  private List<JAuthor> authors;
}
