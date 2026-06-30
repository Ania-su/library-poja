package hei.school.demo.endpoint.rest.controller.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookCopyCreationDto {
  private UUID bookId;
  private String format;
  private Double sellingPrice;
}
