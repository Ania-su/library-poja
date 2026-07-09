package hei.school.demo.endpoint.rest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import hei.school.demo.endpoint.rest.controller.dto.BookCopyCreationDto;
import hei.school.demo.endpoint.rest.controller.dto.BookCopyUpdate;
import hei.school.demo.endpoint.rest.controller.dto.BookStockResponse;
import hei.school.demo.entity.BookCopy;
import hei.school.demo.entity.enums.BookFormat;
import hei.school.demo.exception.NotFoundException;
import hei.school.demo.service.BookCopyService;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = BookCopyController.class)
class BookCopyControllerTest {
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @MockBean private BookCopyService copyService;

  private UUID bookId;
  private UUID copyId;
  private BookCopy bookCopy;

  @BeforeEach
  void setUp() {
    bookId = UUID.randomUUID();
    copyId = UUID.randomUUID();
    bookCopy = new BookCopy();
  }

  @Test
  void getAllBookCopies_ok() throws Exception {
    when(copyService.findAll(bookId, null, null, null)).thenReturn(List.of(bookCopy));

    mockMvc.perform(get("/books/" + bookId + "/copy")).andExpect(status().isOk());
  }

  @Test
  void getAllBookCopies_withFilters_ok() throws Exception {
    BookFormat format = BookFormat.HARDBACK;
    Double minPrice = 10.0;
    Double maxPrice = 20.0;
    when(copyService.findAll(bookId, format, minPrice, maxPrice)).thenReturn(List.of(bookCopy));
    mockMvc
        .perform(
            get("/books/" + bookId + "/copy")
                .param("format", format.name())
                .param("minPrice", minPrice.toString())
                .param("maxPrice", maxPrice.toString()))
        .andExpect(status().isOk());
  }

  @Test
  void getAllBookCopies_shouldReturn400_with_invalidBookId() throws Exception {
    mockMvc.perform(get("/books/not-a-uuid/copy")).andExpect(status().isBadRequest());
  }

  @Test
  void createBookCopy_created() throws Exception {
    BookCopyCreationDto dto = new BookCopyCreationDto();
    when(copyService.save(any())).thenReturn(bookCopy);
    mockMvc
        .perform(
            post("/books/" + bookId + "/copy")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isCreated());
  }

  @Test
  void getStock_ok_withoutDate() throws Exception {
    when(copyService.calculateStock(copyId, null)).thenReturn(new BookStockResponse(copyId, 10));
    mockMvc
        .perform(get("/books/" + bookId + "/copy/" + copyId + "/stock"))
        .andExpect(status().isOk());
  }

  @Test
  void getStock_ok_withDate() throws Exception {
    LocalDate date = LocalDate.now();
    when(copyService.calculateStock(copyId, date)).thenReturn(new BookStockResponse(copyId, 10));
    mockMvc
        .perform(get("/books/" + bookId + "/copy/" + copyId + "/stock").param("t", date.toString()))
        .andExpect(status().isOk());
  }

  @Test
  void getStock_shouldReturn404_with_nonExistingCopy() throws Exception {
    when(copyService.calculateStock(copyId, null))
        .thenThrow(new NotFoundException("Book copy with id " + copyId + " not found"));
    mockMvc
        .perform(get("/books/" + bookId + "/copy/" + copyId + "/stock"))
        .andExpect(status().isNotFound());
  }

  @Test
  void getStock_shouldReturn400_with_invalidCopyId() throws Exception {
    mockMvc
        .perform(get("/books/" + bookId + "/copy/not-a-uuid/stock"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void getBookCopyById_ok() throws Exception {
    when(copyService.getCopyById(copyId)).thenReturn(bookCopy);
    mockMvc.perform(get("/books/" + bookId + "/copy/" + copyId)).andExpect(status().isOk());
  }

  @Test
  void getBookCopyById_shouldReturn400_with_invalidUUID() throws Exception {
    mockMvc
        .perform(get("/books/" + bookId + "/copy/not-a-uuid"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void getBookCopyById_shouldReturn404_with_nonExistingCopy() throws Exception {
    when(copyService.getCopyById(copyId))
        .thenThrow(new NotFoundException("Book copy with id " + copyId + " not found"));
    mockMvc.perform(get("/books/" + bookId + "/copy/" + copyId)).andExpect(status().isNotFound());
  }

  @Test
  void updateBookCopy_ok() throws Exception {
    BookCopyUpdate update = new BookCopyUpdate();
    when(copyService.updateBookCopy(eq(copyId), any())).thenReturn(bookCopy);
    mockMvc
        .perform(
            patch("/books/" + bookId + "/copy/" + copyId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update)))
        .andExpect(status().isOk());
  }

  @Test
  void updateBookCopy_shouldReturn404_with_nonExistingCopy() throws Exception {
    BookCopyUpdate update = new BookCopyUpdate();
    when(copyService.updateBookCopy(eq(copyId), any()))
        .thenThrow(new NotFoundException("Book copy with id " + copyId + " not found"));
    mockMvc
        .perform(
            patch("/books/" + bookId + "/copy/" + copyId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update)))
        .andExpect(status().isNotFound());
  }

  @Test
  void updateBookCopy_shouldReturn400_with_invalidUUID() throws Exception {
    BookCopyUpdate update = new BookCopyUpdate();
    mockMvc
        .perform(
            patch("/books/" + bookId + "/copy/not-a-uuid")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void deleteBookCopy_ok() throws Exception {
    when(copyService.deleteBookCopy(copyId)).thenReturn(bookCopy);
    mockMvc.perform(delete("/books/" + bookId + "/copy/" + copyId)).andExpect(status().isOk());
  }

  @Test
  void deleteBookCopy_shouldReturn404_with_nonExistingCopy() throws Exception {
    when(copyService.deleteBookCopy(copyId))
        .thenThrow(new NotFoundException("Book copy with id " + copyId + " not found"));
    mockMvc
        .perform(delete("/books/" + bookId + "/copy/" + copyId))
        .andExpect(status().isNotFound());
  }

  @Test
  void deleteBookCopy_shouldReturn400_with_invalidUUID() throws Exception {
    mockMvc
        .perform(delete("/books/" + bookId + "/copy/not-a-uuid"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void getTotalStock_ok_withoutDate() throws Exception {
    when(copyService.calculateTotalStock(bookId, null))
        .thenReturn(new BookStockResponse(bookId, 42));
    mockMvc.perform(get("/books/" + bookId + "/copy/stock")).andExpect(status().isOk());
  }

  @Test
  void getTotalStock_ok_withDate() throws Exception {
    LocalDate date = LocalDate.now();
    when(copyService.calculateTotalStock(bookId, date))
        .thenReturn(new BookStockResponse(bookId, 42));
    mockMvc
        .perform(get("/books/" + bookId + "/copy/stock").param("date", date.toString()))
        .andExpect(status().isOk());
  }

  @Test
  void getTotalStock_shouldReturn400_with_invalidBookId() throws Exception {
    mockMvc.perform(get("/books/not-a-uuid/copy/stock")).andExpect(status().isBadRequest());
  }
}
