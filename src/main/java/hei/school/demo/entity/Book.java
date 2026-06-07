package hei.school.demo.entity;

import hei.school.demo.entity.enums.BookFormat;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name="book")
@AllArgsConstructor
@NoArgsConstructor
@Data

public class Book {
  @Id private String id;

  @Column(nullable = false)
  private String title;

  @Column(unique = true)
  private String isbn;

  @Column(name = "publication_date")
  private LocalDate publicationDate;

  @ManyToOne
  @JoinColumn(name = "publisher_id")
  private Publisher publisher;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Enumerated(EnumType.STRING)
  private BookFormat format;

  @OneToMany(mappedBy = "book")
  private List<BookAuthor> bookAuthors;

  @OneToMany(mappedBy = "book")
  private List<BookGenre> genres;

  @OneToMany(mappedBy = "book")
  private List<BookCopy> copies;
}
