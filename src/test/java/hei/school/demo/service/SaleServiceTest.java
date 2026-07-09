package hei.school.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import hei.school.demo.endpoint.rest.controller.dto.SaleItemRequest;
import hei.school.demo.endpoint.rest.controller.dto.SaleRequest;
import hei.school.demo.entity.Sale;
import hei.school.demo.entity.enums.PaymentMethod;
import hei.school.demo.entity.enums.SaleStatus;
import hei.school.demo.exception.NotFoundException;
import hei.school.demo.repository.BookCopyRepository;
import hei.school.demo.repository.CustomerRepository;
import hei.school.demo.repository.SaleRepository;
import hei.school.demo.repository.mapper.SaleMapper;
import hei.school.demo.repository.model.JBookCopy;
import hei.school.demo.repository.model.JCustomer;
import hei.school.demo.repository.model.JSale;
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
class SaleServiceTest {

  @Mock private SaleRepository saleRepository;
  @Mock private CustomerRepository customerRepository;
  @Mock private BookCopyRepository bookCopyRepository;
  @Mock private SaleMapper saleMapper;

  private SaleService saleService;

  private UUID saleId;
  private UUID customerId;
  private UUID bookCopyId;
  private JSale jSale;
  private Sale sale;
  private JCustomer jCustomer;
  private JBookCopy jBookCopy;

  @BeforeEach
  void setUp() {
    saleService =
        new SaleService(saleRepository, customerRepository, bookCopyRepository, saleMapper);

    saleId = UUID.fromString("00000000-0000-0000-0000-111111111111");
    customerId = UUID.fromString("00000000-0000-0000-0000-222222222222");
    bookCopyId = UUID.fromString("00000000-0000-0000-0000-333333333333");

    jCustomer = new JCustomer();
    jCustomer.setId(customerId);
    jCustomer.setFullName("rakodo");

    jBookCopy = new JBookCopy();
    jBookCopy.setId(bookCopyId);
    jBookCopy.setSellingPrice(15.0);

    jSale = new JSale();
    jSale.setId(saleId);
    jSale.setCustomer(jCustomer);
    jSale.setSaleDate(LocalDate.of(2024, 1, 15));
    jSale.setStatus(SaleStatus.PENDING);

    sale = new Sale();
    sale.setId(saleId);
    sale.setSaleDate(LocalDate.of(2024, 1, 15));
    sale.setStatus(SaleStatus.PENDING);
  }

  @Test
  void getSales_shouldReturnMappedSales() {
    when(saleRepository.findAll(PageRequest.of(0, 10))).thenReturn(new PageImpl<>(List.of(jSale)));
    when(saleMapper.toDomain(jSale)).thenReturn(sale);

    var result = saleService.getSales(1, 10);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getId()).isEqualTo(saleId);
    verify(saleRepository).findAll(PageRequest.of(0, 10));
  }

  @Test
  void getSales_shouldReturnEmptyList_whenNoSales() {
    when(saleRepository.findAll(PageRequest.of(0, 10))).thenReturn(new PageImpl<>(List.of()));

    var result = saleService.getSales(1, 10);

    assertThat(result).isEmpty();
  }

  @Test
  void countSales_shouldReturnCount() {
    when(saleRepository.count()).thenReturn(7L);

    var result = saleService.countSales();

    assertThat(result).isEqualTo(7L);
  }

  @Test
  void getSaleById_shouldReturnSale_whenFound() {
    when(saleRepository.findById(saleId)).thenReturn(Optional.of(jSale));
    when(saleMapper.toDomain(jSale)).thenReturn(sale);

    var result = saleService.getSaleById(saleId);

    assertThat(result.getId()).isEqualTo(saleId);
    verify(saleRepository).findById(saleId);
  }

  @Test
  void getSaleById_shouldThrowNotFoundException_whenNotFound() {
    var unknownId = UUID.randomUUID();
    when(saleRepository.findById(unknownId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> saleService.getSaleById(unknownId))
        .isInstanceOf(NotFoundException.class)
        .hasMessage("Sale not found");
  }

  @Test
  void createSale_shouldSaveAndReturnSale() {
    var itemReq = new SaleItemRequest(bookCopyId, 2);
    var request =
        new SaleRequest(
            customerId,
            LocalDate.of(2024, 1, 15),
            PaymentMethod.CASH,
            SaleStatus.PENDING,
            List.of(itemReq));

    when(customerRepository.findById(customerId)).thenReturn(Optional.of(jCustomer));
    when(bookCopyRepository.findById(bookCopyId)).thenReturn(Optional.of(jBookCopy));
    when(saleRepository.save(any(JSale.class))).thenReturn(jSale);
    when(saleMapper.toDomain(jSale)).thenReturn(sale);

    var result = saleService.createSale(request);

    assertThat(result.getId()).isEqualTo(saleId);
    verify(saleRepository).save(any(JSale.class));
    verify(customerRepository).findById(customerId);
    verify(bookCopyRepository).findById(bookCopyId);
  }

  @Test
  void createSale_shouldDefaultToPending_whenStatusIsNull() {
    var itemReq = new SaleItemRequest(bookCopyId, 1);
    var request =
        new SaleRequest(
            customerId,
            LocalDate.of(2024, 1, 15),
            PaymentMethod.CASH,
            SaleStatus.PENDING,
            List.of(itemReq));

    when(customerRepository.findById(customerId)).thenReturn(Optional.of(jCustomer));
    when(bookCopyRepository.findById(bookCopyId)).thenReturn(Optional.of(jBookCopy));
    when(saleRepository.save(any(JSale.class))).thenReturn(jSale);
    when(saleMapper.toDomain(jSale)).thenReturn(sale);

    var result = saleService.createSale(request);

    assertThat(result.getStatus()).isEqualTo(SaleStatus.PENDING);
    verify(saleRepository).save(any(JSale.class));
  }

  @Test
  void createSale_shouldThrowNotFoundException_whenCustomerNotFound() {
    var unknownCustomerId = UUID.randomUUID();
    var request =
        new SaleRequest(
            unknownCustomerId,
            LocalDate.of(2024, 1, 15),
            PaymentMethod.CASH,
            SaleStatus.PENDING,
            List.of());

    when(customerRepository.findById(unknownCustomerId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> saleService.createSale(request))
        .isInstanceOf(NotFoundException.class)
        .hasMessage("Customer not found");

    verify(saleRepository, never()).save(any());
  }

  @Test
  void createSale_shouldThrowNotFoundException_whenBookCopyNotFound() {
    var unknownCopyId = UUID.randomUUID();
    var itemReq = new SaleItemRequest(unknownCopyId, 1);
    var request =
        new SaleRequest(
            customerId,
            LocalDate.of(2024, 1, 15),
            PaymentMethod.CASH,
            SaleStatus.PENDING,
            List.of(itemReq));

    when(customerRepository.findById(customerId)).thenReturn(Optional.of(jCustomer));
    when(bookCopyRepository.findById(unknownCopyId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> saleService.createSale(request))
        .isInstanceOf(NotFoundException.class)
        .hasMessageContaining("Book copy not found");

    verify(saleRepository, never()).save(any());
  }

  @Test
  void updateSale_shouldUpdateAndReturnSale() {
    var request =
        new SaleRequest(
            customerId, LocalDate.of(2024, 6, 1), PaymentMethod.CARD, SaleStatus.PAID, List.of());

    var updatedSale = new Sale();
    updatedSale.setId(saleId);
    updatedSale.setSaleDate(LocalDate.of(2024, 6, 1));
    updatedSale.setStatus(SaleStatus.PAID);

    when(saleRepository.findById(saleId)).thenReturn(Optional.of(jSale));
    when(saleRepository.save(any(JSale.class))).thenReturn(jSale);
    when(saleMapper.toDomain(jSale)).thenReturn(updatedSale);

    var result = saleService.updateSale(saleId, request);

    assertThat(result.getStatus()).isEqualTo(SaleStatus.PAID);
    assertThat(result.getSaleDate()).isEqualTo(LocalDate.of(2024, 6, 1));
    verify(saleRepository).save(any(JSale.class));
  }

  @Test
  void updateSale_shouldThrowNotFoundException_whenNotFound() {
    var unknownId = UUID.randomUUID();
    var request =
        new SaleRequest(
            customerId, LocalDate.of(2024, 6, 1), PaymentMethod.CARD, SaleStatus.PAID, List.of());

    when(saleRepository.findById(unknownId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> saleService.updateSale(unknownId, request))
        .isInstanceOf(NotFoundException.class)
        .hasMessage("Sale not found");

    verify(saleRepository, never()).save(any());
  }

  @Test
  void deleteSale_shouldDeleteSale_whenFound() {
    when(saleRepository.findById(saleId)).thenReturn(Optional.of(jSale));

    saleService.deleteSale(saleId);

    verify(saleRepository).delete(jSale);
  }

  @Test
  void deleteSale_shouldThrowNotFoundException_whenNotFound() {
    var unknownId = UUID.randomUUID();
    when(saleRepository.findById(unknownId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> saleService.deleteSale(unknownId))
        .isInstanceOf(NotFoundException.class)
        .hasMessage("Sale not found");

    verify(saleRepository, never()).delete(any(JSale.class));
  }
}
