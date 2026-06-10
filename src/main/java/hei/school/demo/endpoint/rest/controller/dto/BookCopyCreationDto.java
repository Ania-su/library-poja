package hei.school.demo.endpoint.rest.controller.dto;

import java.math.BigDecimal;

public record BookCopyCreationDto(String bookId, String format, BigDecimal sellingPrice) {}
