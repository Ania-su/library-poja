package hei.school.demo.repository.mapper;

import hei.school.demo.entity.Arrival;
import hei.school.demo.entity.ArrivalItem;
import hei.school.demo.repository.model.JArrival;
import hei.school.demo.repository.model.JArrivalItem;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ArrivalMapper {

  public Arrival toDomain(JArrival jArrival) {
    if (jArrival == null)
      return null;
    Arrival arrival = new Arrival();
    arrival.setId(jArrival.getId());
    arrival.setArrivalDate(jArrival.getArrivalDate());
    if (jArrival.getItems() != null) {
      List<ArrivalItem> items = new ArrayList<>();
      for (JArrivalItem jai : jArrival.getItems()) {
        items.add(toDomain(jai));
      }
      arrival.setItems(items);
    }
    return arrival;
  }

  public ArrivalItem toDomain(JArrivalItem jArrivalItem) {
    if (jArrivalItem == null)
      return null;
    ArrivalItem item = new ArrivalItem();
    item.setId(jArrivalItem.getId());
    item.setArrivalId(jArrivalItem.getArrival().getId());
    item.setBookCopyId(jArrivalItem.getBookCopy().getId());
    return item;
  }

  public JArrival toJpa(Arrival arrival) {
    if (arrival == null)
      return null;
    JArrival jArrival = new JArrival();
    jArrival.setId(arrival.getId());
    jArrival.setArrivalDate(arrival.getArrivalDate());
    // Items mapping usually handled by service or specific logic due to
    // bidirectional relationship
    return jArrival;
  }
}
