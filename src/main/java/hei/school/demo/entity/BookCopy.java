package hei.school.demo.entity;

import hei.school.demo.entity.enums.CopyCondition;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="book_copy")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookCopy {

    @Id
  private String id;

    @ManyToOne
    @JoinColumn(name="book_id", nullable=false)
  private Book book;

    @ManyToOne
    @JoinColumn(name="library_id", nullable=false)
  private Library library;

    @ManyToOne
    @JoinColumn(name="arrival_id")
    private Arrival arrival;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
  private CopyCondition condition;

    @Column(name="purchase_price", nullable=false)
  private double purchasePrice;

    @Column(name="selling_price")
  private double sellingPrice;

  private boolean sold;
}
