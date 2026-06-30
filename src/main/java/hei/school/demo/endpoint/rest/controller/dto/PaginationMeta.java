package hei.school.demo.endpoint.rest.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaginationMeta {
  private long total;
  private int page;
  private int perPage;
}
