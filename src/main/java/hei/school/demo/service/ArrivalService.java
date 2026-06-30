package hei.school.demo.service;

import hei.school.demo.endpoint.rest.controller.dto.ArrivalItemRequest;
import hei.school.demo.endpoint.rest.controller.dto.ArrivalRequest;
import hei.school.demo.endpoint.rest.controller.dto.ArrivalUpdateRequest;
import hei.school.demo.entity.Arrival;
import hei.school.demo.repository.ArrivalRepository;
import hei.school.demo.repository.BookCopyRepository;
import hei.school.demo.repository.mapper.ArrivalMapper;
import hei.school.demo.repository.model.JArrival;
import hei.school.demo.repository.model.JArrivalItem;
import hei.school.demo.repository.model.JBookCopy;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ArrivalService {

  private final ArrivalRepository arrivalRepository;
  private final BookCopyRepository bookCopyRepository;
  private final ArrivalMapper arrivalMapper;

  public List<Arrival> getArrivals(int page, int perPage) {
    Pageable pageable = PageRequest.of(page - 1, perPage);
    return arrivalRepository.findAll(pageable).getContent().stream()
        .map(arrivalMapper::toDomain)
        .collect(Collectors.toList());
  }

  public long countArrivals() {
    return arrivalRepository.count();
  }

  public Arrival getArrivalById(UUID id) {
    JArrival jArrival =
        arrivalRepository.findById(id).orElseThrow(() -> new RuntimeException("Arrival not found"));
    return arrivalMapper.toDomain(jArrival);
  }

  @Transactional
  public Arrival createArrival(ArrivalRequest request) {
    JArrival jArrival = new JArrival();
    jArrival.setArrivalDate(request.getArrivalDate());

    List<JArrivalItem> items = new ArrayList<>();
    for (ArrivalItemRequest itemRequest : request.getItems()) {
      JArrivalItem item = new JArrivalItem();
      item.setArrival(jArrival);

      JBookCopy bookCopy =
          bookCopyRepository
              .findById(itemRequest.getBookCopyId())
              .orElseThrow(
                  () ->
                      new RuntimeException("Book copy not found: " + itemRequest.getBookCopyId()));

      item.setBookCopy(bookCopy);
      item.setQuantity(itemRequest.getQuantity());
      items.add(item);
    }
    jArrival.setItems(items);

    JArrival saved = arrivalRepository.save(jArrival);
    return arrivalMapper.toDomain(saved);
  }

  @Transactional
  public Arrival updateArrival(UUID id, ArrivalUpdateRequest request) {
    JArrival existing =
        arrivalRepository.findById(id).orElseThrow(() -> new RuntimeException("Arrival not found"));
    if (request.getArrivalDate() != null) {
      existing.setArrivalDate(request.getArrivalDate());
    }
    JArrival saved = arrivalRepository.save(existing);
    return arrivalMapper.toDomain(saved);
  }

  public void deleteArrival(UUID id) {
    arrivalRepository.deleteById(id);
  }
}
