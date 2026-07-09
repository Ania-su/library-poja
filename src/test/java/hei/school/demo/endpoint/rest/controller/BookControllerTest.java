package hei.school.demo.endpoint.rest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import hei.school.demo.endpoint.rest.controller.dto.BookRequest;
import hei.school.demo.endpoint.rest.controller.dto.LowStockResponse;
import hei.school.demo.entity.Book;
import hei.school.demo.exception.NotFoundException;
import hei.school.demo.service.BookCopyService;
import hei.school.demo.service.BookService;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = BookController.class)
class BookControllerTest {
  private Book book1;
  private Book book2;

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @MockBean private BookService bookService;

  @MockBean private BookCopyService bookCopyService;

  @BeforeEach
  void setUp() {
    book1 = new Book();
    book2 = new Book();
  }

  @Test
  void getBooks_ok() throws Exception {
    when(bookService.getBooks(null, null, null, null, null, null, 1, 10))
        .thenReturn(List.of(book1, book2));
    when(bookService.countBooks(null, null, null, null, null, null)).thenReturn(2L);
    mockMvc
        .perform(get("/books"))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.books.length()").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.meta.total").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.meta.page").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.meta.perPage").value(10));
  }

  @Test
  void getBooks_shouldReturn400_with_invalidDateFormat() throws Exception {
    mockMvc.perform(get("/books").param("before", "not-a-date")).andExpect(status().isBadRequest());
  }

  @Test
  void getBookById_ok_withExistingBook() throws Exception {
    var id = UUID.randomUUID();
    when(bookService.getBookById(id)).thenReturn(book1);
    mockMvc.perform(get("/books/" + id)).andExpect(status().isOk());
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

  @Test
  void deleteBook_noContent_withExistingBook() throws Exception {
    var id = UUID.randomUUID();
    mockMvc.perform(delete("/books/" + id)).andExpect(status().isNoContent());
    verify(bookService).deleteBook(id);
  }

  @Test
  void deleteBook_shouldReturn404_with_nonExistingBook() throws Exception {
    var randomUUID = UUID.randomUUID();
    doThrow(new NotFoundException("Book with id " + randomUUID + " not found"))
        .when(bookService)
        .deleteBook(randomUUID);
    mockMvc.perform(delete("/books/" + randomUUID)).andExpect(status().isNotFound());
  }

  @Test
  void deleteBook_shouldReturn400_with_invalidUUID() throws Exception {
    mockMvc.perform(delete("/books/1")).andExpect(status().isBadRequest());
  }

  @Test
  void createBook_created_withValidBody() throws Exception {
    BookRequest request = new BookRequest();
    request.setTitle("Clean Code");
    request.setDescription("A handbook of agile software craftsmanship");
    request.setPublicationDate(LocalDate.of(2008, 8, 1));
    request.setAuthors(List.of("author-1"));
    request.setGenres(List.of("genre-1"));

    when(bookService.createBook(any(BookRequest.class))).thenReturn(book1);

    mockMvc
        .perform(
            post("/books")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated());

    verify(bookService).createBook(any(BookRequest.class));
  }

  @Test
  void updateBook_ok_withExistingBook() throws Exception {
    var id = UUID.randomUUID();
    BookRequest request = new BookRequest();
    request.setTitle("Clean Code");
    request.setDescription("A handbook of agile software craftsmanship");
    request.setPublicationDate(LocalDate.of(2008, 8, 1));
    request.setAuthors(List.of("author-1"));
    request.setGenres(List.of("genre-1"));

    when(bookService.updateBook(eq(id), any(BookRequest.class))).thenReturn(book1);

    mockMvc
        .perform(
            put("/books/" + id)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());

    verify(bookService).updateBook(eq(id), any(BookRequest.class));
  }

  @Test
  void updateBook_shouldReturn404_with_nonExistingBook() throws Exception {
    var randomUUID = UUID.randomUUID();
    BookRequest request = new BookRequest();
    request.setTitle("Clean Code");

    when(bookService.updateBook(eq(randomUUID), any(BookRequest.class)))
        .thenThrow(new NotFoundException("Book with id " + randomUUID + " not found"));

    mockMvc
        .perform(
            put("/books/" + randomUUID)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound());
  }

  @Test
  void updateBook_shouldReturn400_with_invalidUUID() throws Exception {
    BookRequest request = new BookRequest();
    request.setTitle("Clean Code");

    mockMvc
        .perform(
            put("/books/1")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void getLowStock_ok_withDefaultThreshold() throws Exception {
    when(bookCopyService.getLowStockCopies(3)).thenReturn(Collections.emptyList());

    mockMvc
        .perform(get("/books/low-stock"))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(0));
  }

  @Test
  void getLowStock_ok_withCustomThreshold() throws Exception {
    when(bookCopyService.getLowStockCopies(5))
        .thenReturn(Collections.nCopies(2, (LowStockResponse) null));

    mockMvc
        .perform(get("/books/low-stock").param("threshold", "5"))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));
  }
}
