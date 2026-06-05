package hei.school.demo.entity;

import hei.school.demo.entity.enums.CopyCondition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookCopy {
  private String id;
  private Book book;
  private Library library;
  private CopyCondition condition;
  private double purchasePrice;
  private double sellingPrice;
  private boolean sold;
}
