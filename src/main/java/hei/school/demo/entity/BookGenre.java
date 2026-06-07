package hei.school.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "book_genre")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookGenre {
  @Id private String id;

  @ManyToOne
  @JoinColumn(name = "book_id", nullable = false)
  private Book book;

  @ManyToOne
  @JoinColumn(name = "genre_id", nullable = false)
  private Genre genre;
}
