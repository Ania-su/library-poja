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
import hei.school.demo.endpoint.rest.controller.dto.CustomerRequest;
import hei.school.demo.entity.Customer;
import hei.school.demo.exception.NotFoundException;
import hei.school.demo.service.CustomerService;
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

@WebMvcTest(controllers = CustomerController.class)
@AutoConfigureMockMvc(addFilters = false)
class CustomerControllerTest {
  private Customer customer1;
  private Customer customer2;

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private CustomerService customerService;

  @BeforeEach
  void setUp() {
    customer1 = new Customer(UUID.randomUUID(), "Ania Su", "ania@gmail.com");
    customer2 = new Customer(UUID.randomUUID(), "Williest Andry", "willi@gmail.com");
  }

  @Test
  void getCustomers_ok() throws Exception {
    when(customerService.getCustomers(1, 10)).thenReturn(List.of(customer1, customer2));
    when(customerService.countCustomers()).thenReturn(2L);

    mockMvc
        .perform(get("/customers"))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.customers.length()").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.meta.total").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.meta.page").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.meta.perPage").value(10));
  }

  @Test
  void getCustomers_ok_withCustomPagination() throws Exception {
    when(customerService.getCustomers(2, 5)).thenReturn(List.of(customer1));
    when(customerService.countCustomers()).thenReturn(6L);

    mockMvc
        .perform(get("/customers").param("page", "2").param("perPage", "5"))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.customers.length()").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.meta.page").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.meta.perPage").value(5));
  }

  @Test
  void createCustomer_created() throws Exception {
    var request = new CustomerRequest("Ania Su", "ania@gmail.com");
    when(customerService.createCustomer(request)).thenReturn(customer1);

    mockMvc
        .perform(
            post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.fullName").value("Ania Su"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("ania@gmail.com"));
  }

  @Test
  void getCustomerById_ok_withExistingCustomer() throws Exception {
    var id = UUID.randomUUID();
    when(customerService.getCustomerById(id)).thenReturn(customer1);

    mockMvc
        .perform(get("/customers/" + id))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.fullName").value("Ania Su"));
  }

  @Test
  void getCustomerById_shouldReturn400_with_invalidUUID() throws Exception {
    mockMvc.perform(get("/customers/1")).andExpect(status().isBadRequest());
  }

  @Test
  void getCustomerById_shouldReturn404_with_nonExistingCustomer() throws Exception {
    var randomUUID = UUID.randomUUID();
    when(customerService.getCustomerById(randomUUID))
        .thenThrow(new NotFoundException("Customer with id " + randomUUID + " not found"));

    mockMvc.perform(get("/customers/" + randomUUID)).andExpect(status().isNotFound());
  }

  @Test
  void updateCustomer_ok_withExistingCustomer() throws Exception {
    var id = UUID.randomUUID();
    var request = new CustomerRequest("Williest Andry", "willi@gmail.com");
    when(customerService.updateCustomer(eq(id), eq(request))).thenReturn(customer2);

    mockMvc
        .perform(
            put("/customers/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.fullName").value("Williest Andry"));
  }

  @Test
  void updateCustomer_shouldReturn404_with_nonExistingCustomer() throws Exception {
    var randomUUID = UUID.randomUUID();
    var request = new CustomerRequest("Williest Andry", "willi@gmail.com");
    when(customerService.updateCustomer(eq(randomUUID), any(CustomerRequest.class)))
        .thenThrow(new NotFoundException("Customer with id " + randomUUID + " not found"));

    mockMvc
        .perform(
            put("/customers/" + randomUUID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound());
  }

  @Test
  void updateCustomer_shouldReturn400_with_invalidUUID() throws Exception {
    var request = new CustomerRequest("Williest Andry", "willi@gmail.com");

    mockMvc
        .perform(
            put("/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void deleteCustomer_noContent_withExistingCustomer() throws Exception {
    var id = UUID.randomUUID();

    mockMvc.perform(delete("/customers/" + id)).andExpect(status().isNoContent());

    verify(customerService).deleteCustomer(id);
  }

  @Test
  void deleteCustomer_shouldReturn404_with_nonExistingCustomer() throws Exception {
    var randomUUID = UUID.randomUUID();
    doThrow(new NotFoundException("Customer with id " + randomUUID + " not found"))
        .when(customerService)
        .deleteCustomer(randomUUID);

    mockMvc.perform(delete("/customers/" + randomUUID)).andExpect(status().isNotFound());
  }

  @Test
  void deleteCustomer_shouldReturn400_with_invalidUUID() throws Exception {
    mockMvc.perform(delete("/customers/1")).andExpect(status().isBadRequest());
  }
}
