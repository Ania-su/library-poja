package hei.school.demo.endpoint.rest.controller.dto;

import java.util.UUID;

public record SaleItemRequest(UUID bookCopyId, int quantity) {}
