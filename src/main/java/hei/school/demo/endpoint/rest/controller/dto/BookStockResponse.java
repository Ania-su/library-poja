package hei.school.demo.endpoint.rest.controller.dto;

import java.util.UUID;

public record BookStockResponse(UUID bookCopyId, int stock) {}
