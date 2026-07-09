package hei.school.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import hei.school.demo.endpoint.rest.controller.dto.ArrivalItemRequest;
import hei.school.demo.endpoint.rest.controller.dto.ArrivalRequest;
import hei.school.demo.endpoint.rest.controller.dto.ArrivalUpdateRequest;
import hei.school.demo.entity.Arrival;
import hei.school.demo.exception.NotFoundException;
import hei.school.demo.repository.ArrivalRepository;
import hei.school.demo.repository.BookCopyRepository;
import hei.school.demo.repository.mapper.ArrivalMapper;
import hei.school.demo.repository.model.JArrival;
import hei.school.demo.repository.model.JBookCopy;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class ArrivalServiceTest {

  @Mock private ArrivalRepository arrivalRepository;
  @Mock private BookCopyRepository bookCopyRepository;
  @Mock private ArrivalMapper arrivalMapper;

  private ArrivalService arrivalService;

  private JArrival jArrival;
  private Arrival arrival;
  private UUID arrivalId;

  @BeforeEach
  void setUp() {
    arrivalService = new ArrivalService(arrivalRepository, bookCopyRepository, arrivalMapper);

    arrivalId = UUID.fromString("00000000-0000-0000-0000-111111111111");
    jArrival = new JArrival();
    jArrival.setId(arrivalId);
    jArrival.setArrivalDate(LocalDate.of(2024, 1, 15));

    arrival = new Arrival();
    arrival.setId(arrivalId);
    arrival.setArrivalDate(LocalDate.of(2024, 1, 15));
  }

  @Test
  void getArrivals_shouldReturnMappedArrivals() {
    when(arrivalRepository.findAll(PageRequest.of(0, 10)))
        .thenReturn(new PageImpl<>(List.of(jArrival)));
    when(arrivalMapper.toDomain(jArrival)).thenReturn(arrival);

    var result = arrivalService.getArrivals(1, 10);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getId()).isEqualTo(arrivalId);
    verify(arrivalRepository).findAll(PageRequest.of(0, 10));
  }

  @Test
  void getArrivals_shouldReturnEmptyList_whenNoArrivals() {
    when(arrivalRepository.findAll(PageRequest.of(0, 10))).thenReturn(new PageImpl<>(List.of()));

    var result = arrivalService.getArrivals(1, 10);

    assertThat(result).isEmpty();
  }

  @Test
  void countArrivals_shouldReturnCount() {
    when(arrivalRepository.count()).thenReturn(5L);

    var result = arrivalService.countArrivals();

    assertThat(result).isEqualTo(5L);
  }

  @Test
  void getArrivalById_shouldReturnArrival_whenFound() {
    when(arrivalRepository.findById(arrivalId)).thenReturn(Optional.of(jArrival));
    when(arrivalMapper.toDomain(jArrival)).thenReturn(arrival);

    var result = arrivalService.getArrivalById(arrivalId);

    assertThat(result.getId()).isEqualTo(arrivalId);
    verify(arrivalRepository).findById(arrivalId);
  }

  @Test
  void getArrivalById_shouldThrowNotFoundException_whenNotFound() {
    var unknownId = UUID.randomUUID();
    when(arrivalRepository.findById(unknownId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> arrivalService.getArrivalById(unknownId))
        .isInstanceOf(NotFoundException.class)
        .hasMessage("Arrival not found");
  }

  @Test
  void createArrival_shouldSaveAndReturnArrival() {
    var bookCopyId = UUID.fromString("00000000-0000-0000-0000-222222222222");
    var jBookCopy = new JBookCopy();
    jBookCopy.setId(bookCopyId);

    var itemRequest = new ArrivalItemRequest();
    itemRequest.setBookCopyId(bookCopyId);
    itemRequest.setQuantity(3);

    var request = new ArrivalRequest();
    request.setArrivalDate(LocalDate.of(2024, 1, 15));
    request.setItems(List.of(itemRequest));

    when(bookCopyRepository.findById(bookCopyId)).thenReturn(Optional.of(jBookCopy));
    when(arrivalRepository.save(any(JArrival.class))).thenReturn(jArrival);
    when(arrivalMapper.toDomain(jArrival)).thenReturn(arrival);

    var result = arrivalService.createArrival(request);

    assertThat(result.getId()).isEqualTo(arrivalId);
    verify(arrivalRepository).save(any(JArrival.class));
    verify(bookCopyRepository).findById(bookCopyId);
  }

  @Test
  void createArrival_shouldThrowNotFoundException_whenBookCopyNotFound() {
    var unknownCopyId = UUID.randomUUID();

    var itemRequest = new ArrivalItemRequest();
    itemRequest.setBookCopyId(unknownCopyId);
    itemRequest.setQuantity(1);

    var request = new ArrivalRequest();
    request.setArrivalDate(LocalDate.of(2024, 1, 15));
    request.setItems(List.of(itemRequest));

    when(bookCopyRepository.findById(unknownCopyId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> arrivalService.createArrival(request))
        .isInstanceOf(NotFoundException.class)
        .hasMessageContaining("Book copy not found");

    verify(arrivalRepository, never()).save(any());
  }

  @Test
  void updateArrival_shouldUpdateDate_whenDateProvided() {
    var request = new ArrivalUpdateRequest();
    request.setArrivalDate(LocalDate.of(2024, 6, 1));

    var updatedJArrival = new JArrival();
    updatedJArrival.setId(arrivalId);
    updatedJArrival.setArrivalDate(LocalDate.of(2024, 6, 1));

    var updatedArrival = new Arrival();
    updatedArrival.setId(arrivalId);
    updatedArrival.setArrivalDate(LocalDate.of(2024, 6, 1));

    when(arrivalRepository.findById(arrivalId)).thenReturn(Optional.of(jArrival));
    when(arrivalRepository.save(any(JArrival.class))).thenReturn(updatedJArrival);
    when(arrivalMapper.toDomain(updatedJArrival)).thenReturn(updatedArrival);

    var result = arrivalService.updateArrival(arrivalId, request);

    assertThat(result.getArrivalDate()).isEqualTo(LocalDate.of(2024, 6, 1));
    verify(arrivalRepository).save(any(JArrival.class));
  }

  @Test
  void updateArrival_shouldNotChangeDate_whenDateIsNull() {
    var request = new ArrivalUpdateRequest();
    request.setArrivalDate(null);

    when(arrivalRepository.findById(arrivalId)).thenReturn(Optional.of(jArrival));
    when(arrivalRepository.save(any(JArrival.class))).thenReturn(jArrival);
    when(arrivalMapper.toDomain(jArrival)).thenReturn(arrival);

    var result = arrivalService.updateArrival(arrivalId, request);

    assertThat(result.getArrivalDate()).isEqualTo(LocalDate.of(2024, 1, 15));
    verify(arrivalRepository).save(any(JArrival.class));
  }

  @Test
  void updateArrival_shouldThrowNotFoundException_whenNotFound() {
    var unknownId = UUID.randomUUID();
    when(arrivalRepository.findById(unknownId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> arrivalService.updateArrival(unknownId, new ArrivalUpdateRequest()))
        .isInstanceOf(NotFoundException.class)
        .hasMessage("Arrival not found");

    verify(arrivalRepository, never()).save(any());
  }

  @Test
  void deleteArrival_shouldCallDeleteById() {
    arrivalService.deleteArrival(arrivalId);

    verify(arrivalRepository).deleteById(arrivalId);
  }
}
