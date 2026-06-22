package hei.school.demo.endpoint.rest.controller;

import hei.school.demo.endpoint.rest.controller.dto.CustomerRequest;
import hei.school.demo.entity.Customer;
import hei.school.demo.service.CustomerService;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

  private final CustomerService customerService;

  @GetMapping
  public ResponseEntity<Map<String, Object>> getCustomers(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int perPage) {
    List<Customer> customers = customerService.getCustomers(page, perPage);
    long total = customerService.countCustomers();
    return ResponseEntity.ok(Map.of(
        "customers", customers,
        "meta", Map.of("total", total, "page", page, "perPage", perPage)
    ));
  }

  @PostMapping
  public ResponseEntity<Customer> createCustomer(@RequestBody CustomerRequest request) {
    Customer created = customerService.createCustomer(request);
    return new ResponseEntity<>(created, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Customer> getCustomerById(@PathVariable UUID id) {
    return ResponseEntity.ok(customerService.getCustomerById(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Customer> updateCustomer(
      @PathVariable UUID id, @RequestBody CustomerRequest request) {
    return ResponseEntity.ok(customerService.updateCustomer(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCustomer(@PathVariable UUID id) {
    customerService.deleteCustomer(id);
    return ResponseEntity.noContent().build();
  }
}
