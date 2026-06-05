package hei.school.demo.entity;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Library {
  private String id;
  private String name;
  private String address;
  private String city;
  private String phone;
  private String email;
  private List<BookCopy> stock;
  private List<Arrival> arrivals;
}
