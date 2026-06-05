package hei.school.demo.entity;

import hei.school.demo.entity.enums.PaymentMethod;
import hei.school.demo.entity.enums.SaleStatus;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Sale {
  private String id;
  private BookCopy bookCopy;
  private Customer customer;
  private Library library;
  private SaleStatus saleStatus;
  private LocalDate saleDate;
  private double finalPrice;
  private double discountPercent;
  private PaymentMethod paymentMethod;
}
