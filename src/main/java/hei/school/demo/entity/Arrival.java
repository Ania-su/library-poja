package hei.school.demo.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="arrival")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Arrival {
    @Id
  private String id;
    @ManyToOne
    @JoinColumn(name="library_id", nullable=false)
  private Library library;
    @Column(name="arrival_date", nullable=false)
  private LocalDate arrivalDate;
    @Column(nullable=false)
  private String supplier;
    @Column(name="invoice_reference")
  private String invoiceReference;
    @OneToMany(mappedBy = "arrival")
  private List<BookCopy> receivedCopies;
}
