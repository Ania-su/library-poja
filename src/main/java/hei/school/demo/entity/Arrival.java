package hei.school.demo.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Arrival {
  private UUID id;
  private LocalDate arrivalDate;
  private List<ArrivalItem> items;
}
