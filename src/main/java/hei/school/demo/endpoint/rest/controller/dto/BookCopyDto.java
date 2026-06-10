package hei.school.demo.endpoint.rest.controller.dto;

import hei.school.demo.entity.enums.BookFormat;
import java.math.BigDecimal;

public record BookCopyDto(
    String bookId, BooksResponse book, BookFormat bookFormat, BigDecimal sellingPrice) {}
