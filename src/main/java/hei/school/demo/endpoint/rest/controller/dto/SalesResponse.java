package hei.school.demo.endpoint.rest.controller.dto;

import hei.school.demo.entity.Sale;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SalesResponse {
  private List<Sale> sales;
  private PaginationMeta meta;
}
