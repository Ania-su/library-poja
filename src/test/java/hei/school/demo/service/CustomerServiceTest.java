package hei.school.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import hei.school.demo.endpoint.rest.controller.dto.CustomerRequest;
import hei.school.demo.entity.Customer;
import hei.school.demo.exception.NotFoundException;
import hei.school.demo.repository.CustomerRepository;
import hei.school.demo.repository.mapper.CustomerMapper;
import hei.school.demo.repository.model.JCustomer;
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
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CustomerMapper customerMapper;

    private CustomerService customerService;

    private UUID customerId;
    private JCustomer jCustomer;
    private Customer customer;

    @BeforeEach
    void setUp() {
        customerService = new CustomerService(customerRepository, customerMapper);

        customerId = UUID.fromString("00000000-0000-0000-0000-111111111111");
        jCustomer = new JCustomer();
        jCustomer.setId(customerId);
        jCustomer.setFullName("rakoto");
        jCustomer.setEmail("rakoto@gmail.com");

        customer = new Customer();
        customer.setId(customerId);
        customer.setFullName("rakoto");
        customer.setEmail("rakoto@gmail.com");
    }

    @Test
    void getCustomers_shouldReturnMappedCustomers() {
        when(customerRepository.findAll(PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of(jCustomer)));
        when(customerMapper.toDomain(jCustomer)).thenReturn(customer);

        var result = customerService.getCustomers(1, 10);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(customerId);
        assertThat(result.get(0).getFullName()).isEqualTo("rakoto");
        verify(customerRepository).findAll(PageRequest.of(0, 10));
    }

    @Test
    void getCustomers_shouldReturnEmptyList_whenNoCustomers() {
        when(customerRepository.findAll(PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of()));

        var result = customerService.getCustomers(1, 10);

        assertThat(result).isEmpty();
    }

    @Test
    void getCustomers_shouldClampPageToZero_whenPageIsNegative() {
        when(customerRepository.findAll(PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of(jCustomer)));
        when(customerMapper.toDomain(jCustomer)).thenReturn(customer);

        var result = customerService.getCustomers(-5, 10);

        assertThat(result).hasSize(1);
        verify(customerRepository).findAll(PageRequest.of(0, 10));
    }

    @Test
    void countCustomers_shouldReturnCount() {
        when(customerRepository.count()).thenReturn(3L);

        var result = customerService.countCustomers();

        assertThat(result).isEqualTo(3L);
    }

    @Test
    void getCustomerById_shouldReturnCustomer_whenFound() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(jCustomer));
        when(customerMapper.toDomain(jCustomer)).thenReturn(customer);

        var result = customerService.getCustomerById(customerId);

        assertThat(result.getId()).isEqualTo(customerId);
        assertThat(result.getFullName()).isEqualTo("rakoto");
        verify(customerRepository).findById(customerId);
    }

    @Test
    void getCustomerById_shouldThrowNotFoundException_whenNotFound() {
        var unknownId = UUID.randomUUID();
        when(customerRepository.findById(unknownId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.getCustomerById(unknownId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Customer not found");
    }

    @Test
    void createCustomer_shouldSaveAndReturnCustomer() {
        var request = new CustomerRequest("rakoto", "rakoto@gmail.com");

        when(customerRepository.save(any(JCustomer.class))).thenReturn(jCustomer);
        when(customerMapper.toDomain(jCustomer)).thenReturn(customer);

        var result = customerService.createCustomer(request);

        assertThat(result.getFullName()).isEqualTo("rakoto");
        assertThat(result.getEmail()).isEqualTo("rakoto@gmail.com");
        verify(customerRepository).save(any(JCustomer.class));
    }

    @Test
    void updateCustomer_shouldUpdateAndReturnCustomer() {
        var request = new CustomerRequest("Jane Doe", "jane@example.com");

        var updatedJCustomer = new JCustomer();
        updatedJCustomer.setId(customerId);
        updatedJCustomer.setFullName("Jane Doe");
        updatedJCustomer.setEmail("jane@example.com");

        var updatedCustomer = new Customer();
        updatedCustomer.setId(customerId);
        updatedCustomer.setFullName("Jane Doe");
        updatedCustomer.setEmail("jane@example.com");

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(jCustomer));
        when(customerRepository.save(any(JCustomer.class))).thenReturn(updatedJCustomer);
        when(customerMapper.toDomain(updatedJCustomer)).thenReturn(updatedCustomer);

        var result = customerService.updateCustomer(customerId, request);

        assertThat(result.getFullName()).isEqualTo("Jane Doe");
        assertThat(result.getEmail()).isEqualTo("jane@example.com");
        verify(customerRepository).save(any(JCustomer.class));
    }

    @Test
    void updateCustomer_shouldThrowNotFoundException_whenNotFound() {
        var unknownId = UUID.randomUUID();
        var request = new CustomerRequest("Jane Doe", "jane@example.com");
        when(customerRepository.findById(unknownId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.updateCustomer(unknownId, request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Customer not found");

        verify(customerRepository, never()).save(any());
    }

    @Test
    void deleteCustomer_shouldDeleteCustomer_whenFound() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(jCustomer));

        customerService.deleteCustomer(customerId);

        verify(customerRepository).delete(jCustomer);
    }

    @Test
    void deleteCustomer_shouldThrowNotFoundException_whenNotFound() {
        var unknownId = UUID.randomUUID();
        when(customerRepository.findById(unknownId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.deleteCustomer(unknownId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Customer not found");

        verify(customerRepository, never()).delete(any(JCustomer.class));
    }
}