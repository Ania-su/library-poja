package hei.school.demo.endpoint.rest.controller.dto;

import hei.school.demo.entity.enums.UserRole;
import lombok.Data;

@Data
public class RegisterRequest {
  private String email;
  private String password;
  private UserRole role;
}
