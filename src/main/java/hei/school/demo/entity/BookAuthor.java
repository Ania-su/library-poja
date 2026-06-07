package hei.school.demo.entity;

import hei.school.demo.entity.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="book_author")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookAuthor {
    @Id
  private String id;

    @ManyToOne
    @JoinColumn(name="book_id", nullable=false)
  private Book book;
    @ManyToOne
    @JoinColumn(name="author_id", nullable=false)
  private Author author;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
  private Role role;
}
