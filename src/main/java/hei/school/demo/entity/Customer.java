package hei.school.demo.entity;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Customer {
  private String id;
  private String name;
  private String lastName;
  private String email;
  private String phone;
  private String address;
  private LocalDate registrationDate;
  private List<Sale> purchases;
}
