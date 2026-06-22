package hei.school.demo.endpoint.rest.controller.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArrivalRequest {
  private LocalDate arrivalDate;
  private List<java.util.UUID> bookCopyIds;
}
