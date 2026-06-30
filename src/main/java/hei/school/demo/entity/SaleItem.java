package hei.school.demo.entity;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SaleItem {
  private UUID id;
  private UUID saleId;
  private UUID bookCopyId;
  private Double unitPrice;
  private Integer quantity;
}
