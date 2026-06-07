package hei.school.demo.entity;

import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "library")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Library {
  @Id private String id;

  @Column(nullable = false)
  private String name;

  private String address;
  private String city;
  private String phone;
  private String email;

  @OneToMany(mappedBy = "library")
  private List<BookCopy> stock;

  @OneToMany(mappedBy = "library")
  private List<Arrival> arrivals;

  @OneToMany(mappedBy = "library")
  private List<Sale> sales;
}
