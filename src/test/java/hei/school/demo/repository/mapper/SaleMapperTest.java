package hei.school.demo.repository.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import hei.school.demo.entity.Sale;
import hei.school.demo.entity.enums.PaymentMethod;
import hei.school.demo.entity.enums.SaleStatus;
import hei.school.demo.repository.model.JBookCopy;
import hei.school.demo.repository.model.JCustomer;
import hei.school.demo.repository.model.JSale;
import hei.school.demo.repository.model.JSaleItem;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SaleMapperTest {

  private SaleMapper saleMapper;
  private JSale jSale;
  private UUID saleId;
  private UUID customerId;

  @BeforeEach
  void setUp() {
    saleMapper = new SaleMapper();
    saleId = UUID.randomUUID();
    customerId = UUID.randomUUID();

    jSale = new JSale();
    jSale.setId(saleId);
    jSale.setSaleDate(LocalDate.of(2026, 1, 15));
    jSale.setStatus(SaleStatus.PAID);
    jSale.setPaymentMethod(PaymentMethod.MOBILE_MONEY);
    jSale.setTotalAmount(75.0);
  }

  @Test
  void toDomain_shouldMapCustomerAndItems() {
    var jCustomer = new JCustomer();
    jCustomer.setId(customerId);
    jCustomer.setFullName("Rina Rasoanaivo");
    jCustomer.setEmail("rina@example.com");
    jSale.setCustomer(jCustomer);

    var bookCopyId = UUID.randomUUID();
    var jBookCopy = new JBookCopy();
    jBookCopy.setId(bookCopyId);

    var jSaleItem = new JSaleItem();
    jSaleItem.setId(UUID.randomUUID());
    jSaleItem.setSale(jSale);
    jSaleItem.setBookCopy(jBookCopy);
    jSaleItem.setUnitPrice(25.0);
    jSaleItem.setQuantity(3);
    jSale.setItems(List.of(jSaleItem));

    var sale = saleMapper.toDomain(jSale);

    assertThat(sale.getId()).isEqualTo(saleId);
    assertThat(sale.getSaleDate()).isEqualTo(LocalDate.of(2026, 1, 15));
    assertThat(sale.getStatus()).isEqualTo(SaleStatus.PAID);
    assertThat(sale.getPaymentMethod()).isEqualTo(PaymentMethod.MOBILE_MONEY);
    assertThat(sale.getTotalAmount()).isEqualTo(75.0);
    assertThat(sale.getCustomer().getId()).isEqualTo(customerId);
    assertThat(sale.getCustomer().getFullName()).isEqualTo("Rina Rasoanaivo");
    assertThat(sale.getCustomer().getEmail()).isEqualTo("rina@example.com");
    assertThat(sale.getItems()).hasSize(1);
    assertThat(sale.getItems().get(0).getSaleId()).isEqualTo(saleId);
    assertThat(sale.getItems().get(0).getBookCopyId()).isEqualTo(bookCopyId);
    assertThat(sale.getItems().get(0).getUnitPrice()).isEqualTo(25.0);
    assertThat(sale.getItems().get(0).getQuantity()).isEqualTo(3);
  }

  @Test
  void toDomain_shouldKeepCustomerNull_whenJSaleHasNoCustomer() {
    var sale = saleMapper.toDomain(jSale);

    assertThat(sale.getCustomer()).isNull();
  }

  @Test
  void toDomain_shouldKeepItemsNull_whenJSaleHasNoItems() {
    var sale = saleMapper.toDomain(jSale);

    assertThat(sale.getItems()).isNull();
  }

  @Test
  void toDomain_shouldReturnNull_whenJSaleIsNull() {
    assertThat(saleMapper.toDomain((JSale) null)).isNull();
  }

  @Test
  void toDomain_shouldMapSaleItem() {
    var bookCopyId = UUID.randomUUID();
    var itemId = UUID.randomUUID();
    var jBookCopy = new JBookCopy();
    jBookCopy.setId(bookCopyId);

    var jSaleItem = new JSaleItem();
    jSaleItem.setId(itemId);
    jSaleItem.setSale(jSale);
    jSaleItem.setBookCopy(jBookCopy);
    jSaleItem.setUnitPrice(25.0);
    jSaleItem.setQuantity(3);

    var item = saleMapper.toDomain(jSaleItem);

    assertThat(item.getId()).isEqualTo(itemId);
    assertThat(item.getSaleId()).isEqualTo(saleId);
    assertThat(item.getBookCopyId()).isEqualTo(bookCopyId);
    assertThat(item.getUnitPrice()).isEqualTo(25.0);
    assertThat(item.getQuantity()).isEqualTo(3);
  }

  @Test
  void toDomain_shouldReturnNull_whenJSaleItemIsNull() {
    assertThat(saleMapper.toDomain((JSaleItem) null)).isNull();
  }

  @Test
  void toJpa_shouldMapScalarFields_butNotCustomerOrItems() {
    var sale =
        new Sale(
            saleId,
            null,
            LocalDate.of(2026, 1, 15),
            SaleStatus.PAID,
            PaymentMethod.CASH,
            75.0,
            List.of());

    var mappedJSale = saleMapper.toJpa(sale);

    assertThat(mappedJSale.getId()).isEqualTo(saleId);
    assertThat(mappedJSale.getSaleDate()).isEqualTo(LocalDate.of(2026, 1, 15));
    assertThat(mappedJSale.getStatus()).isEqualTo(SaleStatus.PAID);
    assertThat(mappedJSale.getPaymentMethod()).isEqualTo(PaymentMethod.CASH);
    assertThat(mappedJSale.getTotalAmount()).isEqualTo(75.0);
    assertThat(mappedJSale.getCustomer()).isNull();
    assertThat(mappedJSale.getItems()).isNull();
  }

  @Test
  void toJpa_shouldReturnNull_whenSaleIsNull() {
    assertThat(saleMapper.toJpa(null)).isNull();
  }
}
