package hei.school.demo.entity;

import hei.school.demo.entity.enums.BookFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "book_copy")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookCopy {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @ManyToOne
  @JoinColumn(name = "book_id", nullable = false)
  private Book book;

  @Enumerated(EnumType.STRING)
  private BookFormat format;

  @Column(name = "selling_price")
  private double sellingPrice;
  //  @ManyToOne
  //  @JoinColumn(name = "library_id", nullable = false)
  //  private Library library;

  //  @ManyToOne
  //  @JoinColumn(name = "arrival_id")
  //  private Arrival arrival;
  //  @Column(name = "purchase_price", nullable = false)
  //  private double purchasePrice;
  //  private boolean sold;
}
