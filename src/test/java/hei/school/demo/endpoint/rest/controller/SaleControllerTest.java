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
import hei.school.demo.endpoint.rest.controller.dto.SaleItemRequest;
import hei.school.demo.endpoint.rest.controller.dto.SaleRequest;
import hei.school.demo.entity.Customer;
import hei.school.demo.entity.Sale;
import hei.school.demo.entity.SaleItem;
import hei.school.demo.entity.enums.PaymentMethod;
import hei.school.demo.entity.enums.SaleStatus;
import hei.school.demo.exception.BadRequestException;
import hei.school.demo.exception.NotFoundException;
import hei.school.demo.service.SaleService;
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

@WebMvcTest(controllers = SaleController.class)
@AutoConfigureMockMvc(addFilters = false)
class SaleControllerTest {
  private Sale sale1;
  private Sale sale2;
  private UUID customerId;
  private UUID bookCopyId;

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @MockBean private SaleService saleService;

  @BeforeEach
  void setUp() {
    customerId = UUID.randomUUID();
    bookCopyId = UUID.randomUUID();
    var customer = new Customer(customerId, "Ania Su", "ania@gmail.com");
    var item = new SaleItem(UUID.randomUUID(), UUID.randomUUID(), bookCopyId, 25.0, 3);

    sale1 =
        new Sale(
            UUID.randomUUID(),
            customer,
            LocalDate.of(2026, 1, 15),
            SaleStatus.PAID,
            PaymentMethod.MOBILE_MONEY,
            75.0,
            List.of(item));
    sale2 =
        new Sale(
            UUID.randomUUID(),
            customer,
            LocalDate.of(2026, 2, 1),
            SaleStatus.RESERVED,
            PaymentMethod.CASH,
            40.0,
            List.of());
  }

  @Test
  void getSales_ok() throws Exception {
    when(saleService.getSales(1, 10)).thenReturn(List.of(sale1, sale2));
    when(saleService.countSales()).thenReturn(2L);
    mockMvc
        .perform(get("/sales"))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.sales.length()").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.sales[0].status").value("PAID"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.meta.total").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.meta.page").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.meta.perPage").value(10));
  }

  @Test
  void getSales_ok_withCustomPagination() throws Exception {
    when(saleService.getSales(2, 5)).thenReturn(List.of(sale2));
    when(saleService.countSales()).thenReturn(6L);

    mockMvc
        .perform(get("/sales").param("page", "2").param("perPage", "5"))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.sales.length()").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.meta.page").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.meta.perPage").value(5));
  }

  @Test
  void createSale_created() throws Exception {
    var request =
        new SaleRequest(
            customerId,
            LocalDate.of(2026, 1, 15),
            PaymentMethod.MOBILE_MONEY,
            SaleStatus.PAID,
            List.of(new SaleItemRequest(bookCopyId, 3)));
    when(saleService.createSale(request)).thenReturn(sale1);

    mockMvc
        .perform(
            post("/sales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("PAID"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.paymentMethod").value("MOBILE_MONEY"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalAmount").value(75.0))
        .andExpect(MockMvcResultMatchers.jsonPath("$.customer.fullName").value("Ania Su"));
  }

  @Test
  void createSale_shouldReturn400_whenServiceRejectsRequest() throws Exception {
    var request =
        new SaleRequest(
            customerId, LocalDate.of(2026, 1, 15), PaymentMethod.CASH, SaleStatus.PAID, List.of());
    when(saleService.createSale(request))
        .thenThrow(new BadRequestException("Sale must contain at least one item"));

    mockMvc
        .perform(
            post("/sales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.message")
                .value("Sale must contain at least one item"));
  }

  @Test
  void getSaleById_ok_withExistingSale() throws Exception {
    var id = UUID.randomUUID();
    when(saleService.getSaleById(id)).thenReturn(sale1);
    mockMvc
        .perform(get("/sales/" + id))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("PAID"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.customer.email").value("ania@gmail.com"));
  }

  @Test
  void getSaleById_shouldReturn400_with_invalidUUID() throws Exception {
    mockMvc.perform(get("/sales/1")).andExpect(status().isBadRequest());
  }

  @Test
  void getSaleById_shouldReturn404_with_nonExistingSale() throws Exception {
    var randomUUID = UUID.randomUUID();
    when(saleService.getSaleById(randomUUID))
        .thenThrow(new NotFoundException("Sale with id " + randomUUID + " not found"));

    mockMvc.perform(get("/sales/" + randomUUID)).andExpect(status().isNotFound());
  }

  @Test
  void updateSale_ok_withExistingSale() throws Exception {
    var id = UUID.randomUUID();
    var request =
        new SaleRequest(
            customerId,
            LocalDate.of(2026, 2, 1),
            PaymentMethod.CASH,
            SaleStatus.RESERVED,
            List.of());
    when(saleService.updateSale(eq(id), eq(request))).thenReturn(sale2);

    mockMvc
        .perform(
            put("/sales/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("RESERVED"));
  }

  @Test
  void updateSale_shouldReturn404_with_nonExistingSale() throws Exception {
    var randomUUID = UUID.randomUUID();
    var request =
        new SaleRequest(
            customerId,
            LocalDate.of(2026, 2, 1),
            PaymentMethod.CASH,
            SaleStatus.RESERVED,
            List.of());
    when(saleService.updateSale(eq(randomUUID), any(SaleRequest.class)))
        .thenThrow(new NotFoundException("Sale with id " + randomUUID + " not found"));
    mockMvc
        .perform(
            put("/sales/" + randomUUID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound());
  }

  @Test
  void updateSale_shouldReturn400_with_invalidUUID() throws Exception {
    var request =
        new SaleRequest(
            customerId,
            LocalDate.of(2026, 2, 1),
            PaymentMethod.CASH,
            SaleStatus.RESERVED,
            List.of());
    mockMvc
        .perform(
            put("/sales/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void deleteSale_noContent_withExistingSale() throws Exception {
    var id = UUID.randomUUID();
    mockMvc.perform(delete("/sales/" + id)).andExpect(status().isNoContent());
    verify(saleService).deleteSale(id);
  }

  @Test
  void deleteSale_shouldReturn404_with_nonExistingSale() throws Exception {
    var randomUUID = UUID.randomUUID();
    doThrow(new NotFoundException("Sale with id " + randomUUID + " not found"))
        .when(saleService)
        .deleteSale(randomUUID);
    mockMvc.perform(delete("/sales/" + randomUUID)).andExpect(status().isNotFound());
  }

  @Test
  void deleteSale_shouldReturn400_with_invalidUUID() throws Exception {
    mockMvc.perform(delete("/sales/1")).andExpect(status().isBadRequest());
  }
}
