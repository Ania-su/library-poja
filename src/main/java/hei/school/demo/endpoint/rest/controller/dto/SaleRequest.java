package hei.school.demo.endpoint.rest.controller.dto;

import hei.school.demo.entity.enums.PaymentMethod;
import hei.school.demo.entity.enums.SaleStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record SaleRequest(
    UUID customerId,
    LocalDate saleDate,
    PaymentMethod paymentMethod,
    SaleStatus status,
    List<SaleItemRequest> items) {

  public record SaleItemRequest(UUID bookCopyId, int quantity) {}
}
