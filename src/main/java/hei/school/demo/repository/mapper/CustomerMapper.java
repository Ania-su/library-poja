package hei.school.demo.repository.mapper;

import hei.school.demo.entity.Customer;
import hei.school.demo.repository.model.JCustomer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

  public Customer toDomain(JCustomer jCustomer) {
    if (jCustomer == null) return null;
    Customer customer = new Customer();
    customer.setId(jCustomer.getId());
    customer.setFullName(jCustomer.getFullName());
    customer.setEmail(jCustomer.getEmail());
    return customer;
  }

  public JCustomer toJpa(Customer customer) {
    if (customer == null) return null;
    JCustomer jCustomer = new JCustomer();
    jCustomer.setId(customer.getId());
    jCustomer.setFullName(customer.getFullName());
    jCustomer.setEmail(customer.getEmail());
    return jCustomer;
  }
}
