package hei.school.demo.endpoint.rest.controller.dto;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LowStockResponse {
  private UUID id;
  private String title;
  private List<LowStockCopy> copies;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class LowStockCopy {
    private UUID id;
    private hei.school.demo.entity.enums.BookFormat format;
    private int stock;
  }
}
