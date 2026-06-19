package hei.school.demo.endpoint.rest.controller.dto;

import hei.school.demo.entity.enums.BookFormat;

public record BookCopyDto(
    String bookId, BooksResponse book, BookFormat bookFormat, Double sellingPrice) {}
