package hei.school.demo.endpoint.rest.controller.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class BookRequest {
  private String title;
  private String description;
  private LocalDate publicationDate;
  private List<String> authors;
  private List<String> genres;
}
