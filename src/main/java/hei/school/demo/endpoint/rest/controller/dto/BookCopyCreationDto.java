package hei.school.demo.endpoint.rest.controller.dto;

import java.math.BigDecimal;
import hei.school.demo.entity.enums.BookFormat;

public record BookCopyCreationDto(
    String bookId,
    BookFormat format,
    BigDecimal sellingPrice) {
}
