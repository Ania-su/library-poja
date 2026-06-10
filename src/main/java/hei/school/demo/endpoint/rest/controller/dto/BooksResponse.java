package hei.school.demo.endpoint.rest.controller.dto;

import hei.school.demo.entity.Book;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BooksResponse {
  private List<Book> books;
  private Meta meta;

  @Data
  @AllArgsConstructor
  public static class Meta {
    private long total;
    private int page;
    private int perPage;
  }
}
