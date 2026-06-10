package hei.school.demo.endpoint.rest.controller.dto;

import hei.school.demo.entity.enums.BookFormat;
import java.math.BigDecimal;
import java.util.UUID;

public record BookCopyDto(
    UUID page, BooksResponse book, BookFormat bookFormat, BigDecimal sellingPrice) {}
