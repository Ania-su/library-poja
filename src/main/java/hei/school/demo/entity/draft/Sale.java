package hei.school.demo.entity.draft;

import hei.school.demo.entity.BookCopy;
import hei.school.demo.entity.enums.PaymentMethod;
import hei.school.demo.entity.enums.SaleStatus;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sale")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Sale {
  @Id private String id;

  @ManyToOne
  @JoinColumn(name = "book_copy_id", nullable = false)
  private BookCopy bookCopy;

  @ManyToOne
  @JoinColumn(name = "customer_id", nullable = false)
  private Customer customer;

  @ManyToOne
  @JoinColumn(name = "library_id", nullable = false)
  private Library library;

  @Enumerated(EnumType.STRING)
  @Column(name = "sale_status", nullable = false)
  private SaleStatus saleStatus;

  @Column(name = "sale_date", nullable = false)
  private LocalDate saleDate;

  @Column(name = "final_price", nullable = false)
  private double finalPrice;

  @Column(name = "discount percent")
  private double discountPercent;

  @Enumerated(EnumType.STRING)
  @Column(name = "payment_method")
  private PaymentMethod paymentMethod;
}
