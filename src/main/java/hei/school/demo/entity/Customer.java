package hei.school.demo.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="customer")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Customer {
    @Id
  private String id;
    @Column(name="first_name", nullable=false)
  private String firstname;
    @Column(name="last_name", nullable=false)
  private String lastName;
    @Column(unique=true)
  private String email;
  private String phone;
  private String address;
  @Column(name="registration_date")
  private LocalDate registrationDate;
  @OneToMany(mappedBy = "customer")
  private List<Sale> purchases;
}
