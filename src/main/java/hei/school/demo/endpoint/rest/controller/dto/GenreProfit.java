package hei.school.demo.endpoint.rest.controller.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GenreProfit {
  private final String name;
  private final Double profit;
}
