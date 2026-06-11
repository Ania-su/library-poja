package hei.school.demo.endpoint.rest.controller.dto;

import hei.school.demo.entity.enums.BookFormat;
import lombok.Data;

@Data
public class BookCopyUpdate {
  private BookFormat format;
  private Double sellingPrice;
}
