package hei.school.demo.entity;

import java.util.List;
import java.util.UUID;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
class Arrival {
  private UUID id;
  private List<ArrivalItem> ArivalCopy;
  private LocalDate arrivalDate;
}
