package hei.school.demo.entity;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ArrivalItem {
  private UUID id;
  private UUID arrivalId;
  private UUID bookCopyId;
}
