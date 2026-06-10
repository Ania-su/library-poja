package hei.school.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hei.school.demo.entity.enums.BookFormat;
import jakarta.persistence.*;
import java.math.BigDecimal;
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
public class BookCopy {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @ManyToOne
  @JoinColumn(name = "book_id", nullable = false)
  @JsonIgnore
  private Book book;

  @Enumerated(EnumType.STRING)
  private BookFormat format;

  @Column(name = "selling_price")
  private BigDecimal sellingPrice;
  // @ManyToOne
  // @JoinColumn(name = "library_id", nullable = false)
  // private Library library;

  // @ManyToOne
  // @JoinColumn(name = "arrival_id")
  // private Arrival arrival;
  // @Column(name = "purchase_price", nullable = false)
  // private double purchasePrice;
  // private boolean sold;
}
