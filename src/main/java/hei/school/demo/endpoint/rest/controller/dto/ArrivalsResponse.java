package hei.school.demo.endpoint.rest.controller.dto;

import hei.school.demo.entity.Arrival;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ArrivalsResponse {
  private List<Arrival> arrivals;
  private PaginationMeta meta;
}
