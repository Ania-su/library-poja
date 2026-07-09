package hei.school.demo.endpoint.rest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import hei.school.demo.endpoint.rest.controller.dto.ArrivalItemRequest;
import hei.school.demo.endpoint.rest.controller.dto.ArrivalRequest;
import hei.school.demo.endpoint.rest.controller.dto.ArrivalUpdateRequest;
import hei.school.demo.entity.Arrival;
import hei.school.demo.entity.ArrivalItem;
import hei.school.demo.exception.NotFoundException;
import hei.school.demo.service.ArrivalService;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = ArrivalController.class)
@AutoConfigureMockMvc(addFilters = false)
class ArrivalControllerTest {
  private Arrival arrival1;
  private Arrival arrival2;
  private UUID bookCopyId;

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private ArrivalService arrivalService;

  @BeforeEach
  void setUp() {
    bookCopyId = UUID.randomUUID();
    var item = new ArrivalItem(UUID.randomUUID(), UUID.randomUUID(), bookCopyId, 5);
    arrival1 = new Arrival(UUID.randomUUID(), LocalDate.of(2026, 1, 15), List.of(item));
    arrival2 = new Arrival(UUID.randomUUID(), LocalDate.of(2026, 2, 1), List.of());
  }

  @Test
  void getArrivals_ok() throws Exception {
    when(arrivalService.getArrivals(1, 10)).thenReturn(List.of(arrival1, arrival2));
    when(arrivalService.countArrivals()).thenReturn(2L);

    mockMvc
        .perform(get("/arrivals"))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.arrivals.length()").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.arrivals[0].arrivalDate").value("2026-01-15"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.meta.total").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.meta.page").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.meta.perPage").value(10));
  }

  @Test
  void getArrivals_ok_withCustomPagination() throws Exception {
    when(arrivalService.getArrivals(2, 5)).thenReturn(List.of(arrival1));
    when(arrivalService.countArrivals()).thenReturn(6L);

    mockMvc
        .perform(get("/arrivals").param("page", "2").param("perPage", "5"))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.arrivals.length()").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.meta.page").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.meta.perPage").value(5));
  }

  @Test
  void createArrival_created() throws Exception {
    var request =
        new ArrivalRequest(
            LocalDate.of(2026, 3, 10), List.of(new ArrivalItemRequest(bookCopyId, 10)));
    when(arrivalService.createArrival(request)).thenReturn(arrival1);

    mockMvc
        .perform(
            post("/arrivals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.arrivalDate").value("2026-01-15"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.items.length()").value(1));
  }

  @Test
  void getArrivalById_ok_withExistingArrival() throws Exception {
    var id = UUID.randomUUID();
    when(arrivalService.getArrivalById(id)).thenReturn(arrival1);

    mockMvc
        .perform(get("/arrivals/" + id))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.arrivalDate").value("2026-01-15"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.items[0].bookCopyId").value(bookCopyId.toString()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].quantity").value(5));
  }

  @Test
  void getArrivalById_shouldReturn400_with_invalidUUID() throws Exception {
    mockMvc.perform(get("/arrivals/1")).andExpect(status().isBadRequest());
  }

  @Test
  void getArrivalById_shouldReturn404_with_nonExistingArrival() throws Exception {
    var randomUUID = UUID.randomUUID();
    when(arrivalService.getArrivalById(randomUUID))
        .thenThrow(new NotFoundException("Arrival with id " + randomUUID + " not found"));

    mockMvc.perform(get("/arrivals/" + randomUUID)).andExpect(status().isNotFound());
  }

  @Test
  void updateArrival_ok_withExistingArrival() throws Exception {
    var id = UUID.randomUUID();
    var request = new ArrivalUpdateRequest(LocalDate.of(2026, 4, 1));
    when(arrivalService.updateArrival(eq(id), eq(request))).thenReturn(arrival2);

    mockMvc
        .perform(
            put("/arrivals/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.arrivalDate").value("2026-02-01"));
  }

  @Test
  void updateArrival_shouldReturn404_with_nonExistingArrival() throws Exception {
    var randomUUID = UUID.randomUUID();
    var request = new ArrivalUpdateRequest(LocalDate.of(2026, 4, 1));
    when(arrivalService.updateArrival(eq(randomUUID), any(ArrivalUpdateRequest.class)))
        .thenThrow(new NotFoundException("Arrival with id " + randomUUID + " not found"));

    mockMvc
        .perform(
            put("/arrivals/" + randomUUID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound());
  }

  @Test
  void updateArrival_shouldReturn400_with_invalidUUID() throws Exception {
    var request = new ArrivalUpdateRequest(LocalDate.of(2026, 4, 1));

    mockMvc
        .perform(
            put("/arrivals/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void deleteArrival_noContent_withExistingArrival() throws Exception {
    var id = UUID.randomUUID();

    mockMvc.perform(delete("/arrivals/" + id)).andExpect(status().isNoContent());

    verify(arrivalService).deleteArrival(id);
  }

  @Test
  void deleteArrival_shouldReturn404_with_nonExistingArrival() throws Exception {
    var randomUUID = UUID.randomUUID();
    doThrow(new NotFoundException("Arrival with id " + randomUUID + " not found"))
        .when(arrivalService)
        .deleteArrival(randomUUID);

    mockMvc.perform(delete("/arrivals/" + randomUUID)).andExpect(status().isNotFound());
  }

  @Test
  void deleteArrival_shouldReturn400_with_invalidUUID() throws Exception {
    mockMvc.perform(delete("/arrivals/1")).andExpect(status().isBadRequest());
  }
}
