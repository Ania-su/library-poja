package hei.school.demo.endpoint.rest.controller;

import hei.school.demo.entity.Book;
import hei.school.demo.exception.NotFoundException;
import hei.school.demo.service.BookCopyService;
import hei.school.demo.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {
    private Book book1;
    private Book book2;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean private BookCopyService bookCopyService;

    @BeforeEach
    void setUp() {
       book1 = new Book();
       book2 = new Book();
    }

    @Test
    void getBooks_ok() throws Exception {
        when(bookService.getBooks(null, null, null, null, null, null, 1, 10)).thenReturn(List.of(book1, book2));
        when(bookService.countBooks(null,null, null, null, null, null  )).thenReturn(2L);
        mockMvc
                .perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.books.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.total").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.page").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.perPage").value(10));
    }

    @Test
    void getBookById_shouldReturn400_with_invalidUUID() throws Exception {
        mockMvc.perform(get("/books/1")).andExpect(status().isBadRequest());
    }

    @Test
    void getBookById_shouldReturn404_with_nonExistingBook() throws Exception {
        var randomUUID = UUID.randomUUID();
        when(bookService.getBookById(randomUUID))
                .thenThrow(new NotFoundException("Book with id " + randomUUID + " not found"));

        mockMvc.perform(get("/books/" + randomUUID)).andExpect(status().isNotFound());
    }
}