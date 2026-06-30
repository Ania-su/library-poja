package hei.school.demo.service;

import hei.school.demo.endpoint.rest.controller.dto.CustomerRequest;
import hei.school.demo.entity.Customer;
import hei.school.demo.repository.CustomerRepository;
import hei.school.demo.repository.mapper.CustomerMapper;
import hei.school.demo.repository.model.JCustomer;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CustomerService {

  private final CustomerRepository customerRepository;
  private final CustomerMapper customerMapper;

  public List<Customer> getCustomers(int page, int perPage) {
    int pageIndex = Math.max(page - 1, 0);
    List<JCustomer> jCustomers =
        customerRepository.findAll(PageRequest.of(pageIndex, perPage)).getContent();
    return jCustomers.stream().map(customerMapper::toDomain).toList();
  }

  public long countCustomers() {
    return customerRepository.count();
  }

  public Customer getCustomerById(UUID id) {
    JCustomer jCustomer =
        customerRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Customer not found"));
    return customerMapper.toDomain(jCustomer);
  }

  @Transactional
  public Customer createCustomer(CustomerRequest request) {
    JCustomer jCustomer = new JCustomer();
    jCustomer.setFullName(request.fullName());
    jCustomer.setEmail(request.email());
    JCustomer saved = customerRepository.save(jCustomer);
    return customerMapper.toDomain(saved);
  }

  @Transactional
  public Customer updateCustomer(UUID id, CustomerRequest request) {
    JCustomer jCustomer =
        customerRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Customer not found"));
    jCustomer.setFullName(request.fullName());
    jCustomer.setEmail(request.email());
    JCustomer saved = customerRepository.save(jCustomer);
    return customerMapper.toDomain(saved);
  }

  @Transactional
  public void deleteCustomer(UUID id) {
    JCustomer jCustomer =
        customerRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Customer not found"));
    customerRepository.delete(jCustomer);
  }
}
