package hei.school.demo.service;

import hei.school.demo.endpoint.rest.controller.dto.SaleItemRequest;
import hei.school.demo.endpoint.rest.controller.dto.SaleRequest;
import hei.school.demo.entity.Sale;
import hei.school.demo.entity.enums.SaleStatus;
import hei.school.demo.repository.BookCopyRepository;
import hei.school.demo.repository.CustomerRepository;
import hei.school.demo.repository.SaleRepository;
import hei.school.demo.repository.mapper.SaleMapper;
import hei.school.demo.repository.model.JBookCopy;
import hei.school.demo.repository.model.JCustomer;
import hei.school.demo.repository.model.JSale;
import hei.school.demo.repository.model.JSaleItem;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class SaleService {

  private final SaleRepository saleRepository;
  private final CustomerRepository customerRepository;
  private final BookCopyRepository bookCopyRepository;
  private final SaleMapper saleMapper;

  public List<Sale> getSales(int page, int perPage) {
    int pageIndex = Math.max(page - 1, 0);
    List<JSale> jSales = saleRepository.findAll(PageRequest.of(pageIndex, perPage)).getContent();
    return jSales.stream().map(saleMapper::toDomain).toList();
  }

  public long countSales() {
    return saleRepository.count();
  }

  public Sale getSaleById(UUID id) {
    JSale jSale =
        saleRepository.findById(id).orElseThrow(() -> new RuntimeException("Sale not found"));
    return saleMapper.toDomain(jSale);
  }

  @Transactional
  public Sale createSale(SaleRequest request) {
    JCustomer jCustomer =
        customerRepository
            .findById(request.customerId())
            .orElseThrow(() -> new RuntimeException("Customer not found"));

    JSale jSale = new JSale();
    jSale.setCustomer(jCustomer);
    jSale.setSaleDate(request.saleDate());
    jSale.setStatus(request.status() != null ? request.status() : SaleStatus.PENDING);
    jSale.setPaymentMethod(request.paymentMethod());

    List<JSaleItem> items = new ArrayList<>();
    double total = 0.0;

    for (SaleItemRequest itemReq : request.items()) {
      JBookCopy jBookCopy =
          bookCopyRepository
              .findById(itemReq.bookCopyId())
              .orElseThrow(
                  () -> new RuntimeException("Book copy not found: " + itemReq.bookCopyId()));

      JSaleItem item = new JSaleItem();
      item.setSale(jSale);
      item.setBookCopy(jBookCopy);
      item.setUnitPrice(jBookCopy.getSellingPrice());
      item.setQuantity(itemReq.quantity());
      items.add(item);

      total += jBookCopy.getSellingPrice() * itemReq.quantity();
    }

    jSale.setItems(items);
    jSale.setTotalAmount(total);

    JSale saved = saleRepository.save(jSale);
    return saleMapper.toDomain(saved);
  }

  @Transactional
  public Sale updateSale(UUID id, SaleRequest request) {
    JSale jSale =
        saleRepository.findById(id).orElseThrow(() -> new RuntimeException("Sale not found"));

    if (request.saleDate() != null) jSale.setSaleDate(request.saleDate());
    if (request.status() != null) jSale.setStatus(request.status());
    if (request.paymentMethod() != null) jSale.setPaymentMethod(request.paymentMethod());

    JSale saved = saleRepository.save(jSale);
    return saleMapper.toDomain(saved);
  }

  @Transactional
  public void deleteSale(UUID id) {
    JSale jSale =
        saleRepository.findById(id).orElseThrow(() -> new RuntimeException("Sale not found"));
    saleRepository.delete(jSale);
  }
}
