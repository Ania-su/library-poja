package hei.school.demo.entity;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Arrival {
  private String id;
  private Library library;
  private LocalDate arrivalDate;
  private String supplier;
  private String invoiceReference;
  private List<BookCopy> receivedCopies;
}
