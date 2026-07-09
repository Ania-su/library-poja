package hei.school.demo.repository.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import hei.school.demo.entity.Arrival;
import hei.school.demo.repository.model.JArrival;
import hei.school.demo.repository.model.JArrivalItem;
import hei.school.demo.repository.model.JBookCopy;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ArrivalMapperTest {

  private ArrivalMapper arrivalMapper;

  @BeforeEach
  void setUp() {
    arrivalMapper = new ArrivalMapper();
  }

  @Test
  void toDomain_shouldMapArrivalWithItems() {
    var arrivalId = UUID.randomUUID();
    var bookCopyId = UUID.randomUUID();
    var jArrival = new JArrival();
    jArrival.setId(arrivalId);
    jArrival.setArrivalDate(LocalDate.of(2026, 1, 15));

    var jBookCopy = new JBookCopy();
    jBookCopy.setId(bookCopyId);

    var jArrivalItem = new JArrivalItem();
    jArrivalItem.setId(UUID.randomUUID());
    jArrivalItem.setArrival(jArrival);
    jArrivalItem.setBookCopy(jBookCopy);
    jArrivalItem.setQuantity(7);
    jArrival.setItems(List.of(jArrivalItem));

    var arrival = arrivalMapper.toDomain(jArrival);

    assertThat(arrival.getId()).isEqualTo(arrivalId);
    assertThat(arrival.getArrivalDate()).isEqualTo(LocalDate.of(2026, 1, 15));
    assertThat(arrival.getItems()).hasSize(1);
    assertThat(arrival.getItems().get(0).getArrivalId()).isEqualTo(arrivalId);
    assertThat(arrival.getItems().get(0).getBookCopyId()).isEqualTo(bookCopyId);
    assertThat(arrival.getItems().get(0).getQuantity()).isEqualTo(7);
  }

  @Test
  void toDomain_shouldKeepItemsNull_whenJArrivalHasNoItems() {
    var jArrival = new JArrival();
    jArrival.setId(UUID.randomUUID());
    jArrival.setArrivalDate(LocalDate.of(2026, 1, 15));

    var arrival = arrivalMapper.toDomain(jArrival);

    assertThat(arrival.getItems()).isNull();
  }

  @Test
  void toDomain_shouldReturnNull_whenJArrivalIsNull() {
    assertThat(arrivalMapper.toDomain((JArrival) null)).isNull();
  }

  @Test
  void toDomain_shouldMapArrivalItem() {
    var arrivalId = UUID.randomUUID();
    var bookCopyId = UUID.randomUUID();
    var itemId = UUID.randomUUID();

    var jArrival = new JArrival();
    jArrival.setId(arrivalId);
    var jBookCopy = new JBookCopy();
    jBookCopy.setId(bookCopyId);

    var jArrivalItem = new JArrivalItem();
    jArrivalItem.setId(itemId);
    jArrivalItem.setArrival(jArrival);
    jArrivalItem.setBookCopy(jBookCopy);
    jArrivalItem.setQuantity(3);

    var item = arrivalMapper.toDomain(jArrivalItem);

    assertThat(item.getId()).isEqualTo(itemId);
    assertThat(item.getArrivalId()).isEqualTo(arrivalId);
    assertThat(item.getBookCopyId()).isEqualTo(bookCopyId);
    assertThat(item.getQuantity()).isEqualTo(3);
  }

  @Test
  void toDomain_shouldReturnNull_whenJArrivalItemIsNull() {
    assertThat(arrivalMapper.toDomain((JArrivalItem) null)).isNull();
  }

  @Test
  void toJpa_shouldMapIdAndArrivalDate_butNotItems() {
    var id = UUID.randomUUID();
    var date = LocalDate.of(2026, 3, 1);
    var arrival = new Arrival(id, date, List.of());

    var jArrival = arrivalMapper.toJpa(arrival);

    assertThat(jArrival.getId()).isEqualTo(id);
    assertThat(jArrival.getArrivalDate()).isEqualTo(date);
    assertThat(jArrival.getItems()).isNull();
  }

  @Test
  void toJpa_shouldReturnNull_whenArrivalIsNull() {
    assertThat(arrivalMapper.toJpa(null)).isNull();
  }
}
