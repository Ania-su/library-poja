package hei.school.demo.repository.mapper;

import hei.school.demo.entity.Customer;
import hei.school.demo.entity.Sale;
import hei.school.demo.entity.SaleItem;
import hei.school.demo.repository.model.JSale;
import hei.school.demo.repository.model.JSaleItem;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SaleMapper {

  public Sale toDomain(JSale jSale) {
    if (jSale == null) return null;
    Sale sale = new Sale();
    sale.setId(jSale.getId());
    sale.setSaleDate(jSale.getSaleDate());
    sale.setStatus(jSale.getStatus());
    sale.setPaymentMethod(jSale.getPaymentMethod());
    sale.setTotalAmount(jSale.getTotalAmount());
    if (jSale.getCustomer() != null) {
      Customer customer = new Customer();
      customer.setId(jSale.getCustomer().getId());
      customer.setFullName(jSale.getCustomer().getFullName());
      customer.setEmail(jSale.getCustomer().getEmail());
      sale.setCustomer(customer);
    }
    if (jSale.getItems() != null) {
      List<SaleItem> items = new ArrayList<>();
      for (JSaleItem jItem : jSale.getItems()) {
        items.add(toDomain(jItem));
      }
      sale.setItems(items);
    }
    return sale;
  }

  public SaleItem toDomain(JSaleItem jSaleItem) {
    if (jSaleItem == null) return null;
    SaleItem item = new SaleItem();
    item.setId(jSaleItem.getId());
    item.setSaleId(jSaleItem.getSale().getId());
    item.setBookCopyId(jSaleItem.getBookCopy().getId());
    item.setUnitPrice(jSaleItem.getUnitPrice());
    item.setQuantity(jSaleItem.getQuantity());
    return item;
  }

  public JSale toJpa(Sale sale) {
    if (sale == null) return null;
    JSale jSale = new JSale();
    jSale.setId(sale.getId());
    jSale.setSaleDate(sale.getSaleDate());
    jSale.setStatus(sale.getStatus());
    jSale.setPaymentMethod(sale.getPaymentMethod());
    jSale.setTotalAmount(sale.getTotalAmount());
    return jSale;
  }
}
