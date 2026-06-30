package hei.school.demo.repository.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "arrival")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JArrival {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "arrival_date", nullable = false)
  private LocalDate arrivalDate;

  @OneToMany(mappedBy = "arrival", cascade = CascadeType.ALL, orphanRemoval = true)
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private List<JArrivalItem> items;
}
