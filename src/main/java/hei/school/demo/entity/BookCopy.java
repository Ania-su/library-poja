package hei.school.demo.entity;

import hei.school.demo.entity.enums.BookFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class BookCopy {

  private String id;
  private Book book;
  private BookFormat format;
  private Double sellingPrice;
}
