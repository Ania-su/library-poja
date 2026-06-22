package hei.school.demo.entity;

import hei.school.demo.entity.enums.PaymentMethod;
import hei.school.demo.entity.enums.SaleStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Sale {
  private UUID id;
  private Customer customer;
  private LocalDate saleDate;
  private SaleStatus status;
  private PaymentMethod paymentMethod;
  private Double totalAmount;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private List<SaleItem> items;
}
