package hei.school.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
  @JsonIgnore
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private List<BookCopy> copies;

  @ManyToMany
  @JoinTable(
    name = "book_genre",
    joinColumns = @JoinColumn(name = "book_id"),
    inverseJoinColumns = @JoinColumn(name="genre_id")
  )
  private List<Genre> genres;

  @ManyToMany
  @JoinTable(
    name = "book_author",
    joinColumns = @JoinColumn(name ="book_id"),
    inverseJoinColumns = @JoinColumn(name = "author_id")
  )
  private List<Author> authors;
}
