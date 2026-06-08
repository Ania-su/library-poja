package hei.school.demo.entity;

import hei.school.demo.entity.draft.Publisher;
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
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;

  @Column(nullable = false)
  private String title;

  //  @Column(unique = true)
  //  private String isbn;

  @Column(name = "publication_date")
  private LocalDate publicationDate;

  @ManyToOne
  @JoinColumn(name = "publisher_id")
  private Publisher publisher;

  @Column(columnDefinition = "TEXT")
  private String description;

  @OneToMany(mappedBy = "book")
  private List<Author> bookAuthors;

  @OneToMany(mappedBy = "book")
  private List<Genre> genres;

  @OneToMany(mappedBy = "book")
  private List<BookCopy> copies;
}
