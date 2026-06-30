package hei.school.demo.repository.model;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "arrival_item")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JArrivalItem {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "arrival_id", nullable = false)
  private JArrival arrival;

  @ManyToOne
  @JoinColumn(name = "book_copy_id", nullable = false)
  private JBookCopy bookCopy;

  private int quantity;
}
