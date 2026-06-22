package hei.school.demo.endpoint.rest.controller.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArrivalItemRequest {
  private UUID bookCopyId;
  private int quantity;
}
