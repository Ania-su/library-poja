package hei.school.demo.endpoint.rest.controller;

import hei.school.demo.endpoint.rest.controller.dto.SaleRequest;
import hei.school.demo.endpoint.rest.controller.dto.SalesResponse;
import hei.school.demo.entity.Sale;
import hei.school.demo.service.SaleService;
import java.util.List;
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
@RequestMapping("/sales")
@RequiredArgsConstructor
public class SaleController {

  private final SaleService saleService;

  @GetMapping
  public ResponseEntity<SalesResponse> getSales(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int perPage) {
    List<Sale> sales = saleService.getSales(page, perPage);
    long total = saleService.countSales();
    return ResponseEntity.ok(new SalesResponse(sales, new SalesResponse.Meta(total, page, perPage)));
  }

  @PostMapping
  public ResponseEntity<Sale> createSale(@RequestBody SaleRequest request) {
    Sale created = saleService.createSale(request);
    return new ResponseEntity<>(created, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Sale> getSaleById(@PathVariable UUID id) {
    return ResponseEntity.ok(saleService.getSaleById(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Sale> updateSale(@PathVariable UUID id, @RequestBody SaleRequest request) {
    return ResponseEntity.ok(saleService.updateSale(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteSale(@PathVariable UUID id) {
    saleService.deleteSale(id);
    return ResponseEntity.noContent().build();
  }
}
