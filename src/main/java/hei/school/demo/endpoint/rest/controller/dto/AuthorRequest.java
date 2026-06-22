package hei.school.demo.endpoint.rest.controller.dto;

import lombok.Data;

@Data
public class AuthorRequest {
  private String firstName;
  private String lastName;
  private String biography;
}
