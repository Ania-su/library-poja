package hei.school.demo.endpoint.rest.controller.dto;

import java.util.UUID;

public record BookCopyCreationDto(UUID bookId, String format, Double sellingPrice) {}
