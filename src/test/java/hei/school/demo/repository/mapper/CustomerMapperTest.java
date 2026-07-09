package hei.school.demo.repository.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import hei.school.demo.entity.Customer;
import hei.school.demo.repository.model.JCustomer;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomerMapperTest {

  private CustomerMapper customerMapper;

  @BeforeEach
  void setUp() {
    customerMapper = new CustomerMapper();
  }

  @Test
  void toDomain_shouldMapAllFields() {
    var id = UUID.randomUUID();
    var jCustomer = new JCustomer();
    jCustomer.setId(id);
    jCustomer.setFullName("Rina Rasoanaivo");
    jCustomer.setEmail("rina@example.com");

    var customer = customerMapper.toDomain(jCustomer);

    assertThat(customer.getId()).isEqualTo(id);
    assertThat(customer.getFullName()).isEqualTo("Rina Rasoanaivo");
    assertThat(customer.getEmail()).isEqualTo("rina@example.com");
  }

  @Test
  void toDomain_shouldReturnNull_whenInputIsNull() {
    assertThat(customerMapper.toDomain(null)).isNull();
  }

  @Test
  void toJpa_shouldMapAllFields() {
    var id = UUID.randomUUID();
    var customer = new Customer(id, "Rina Rasoanaivo", "rina@example.com");

    var jCustomer = customerMapper.toJpa(customer);

    assertThat(jCustomer.getId()).isEqualTo(id);
    assertThat(jCustomer.getFullName()).isEqualTo("Rina Rasoanaivo");
    assertThat(jCustomer.getEmail()).isEqualTo("rina@example.com");
  }

  @Test
  void toJpa_shouldReturnNull_whenInputIsNull() {
    assertThat(customerMapper.toJpa(null)).isNull();
  }
}
