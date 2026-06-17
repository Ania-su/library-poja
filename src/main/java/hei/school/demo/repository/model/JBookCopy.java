package hei.school.demo.repository.model;

import hei.school.demo.entity.enums.BookFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "book_copy")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class JBookCopy {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @ManyToOne
  @JoinColumn(name = "book_id", nullable = false)
  private JBook book;

  @Enumerated(EnumType.STRING)
  private BookFormat format;

  @Column(name = "selling_price")
  private Double sellingPrice;
}
