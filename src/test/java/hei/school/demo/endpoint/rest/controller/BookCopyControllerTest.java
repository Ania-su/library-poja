package hei.school.demo.endpoint.rest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import hei.school.demo.config.JwtAuthFilter;
import hei.school.demo.config.SecurityConfig;
import hei.school.demo.entity.BookCopy;
import hei.school.demo.entity.enums.BookFormat;
import hei.school.demo.endpoint.rest.controller.dto.BookCopyCreationDto;
import hei.school.demo.endpoint.rest.controller.dto.BookCopyUpdate;
import hei.school.demo.endpoint.rest.controller.dto.BookStockResponse;
import hei.school.demo.service.BookCopyService;
import hei.school.demo.service.CustomUserDetailsService;
import hei.school.demo.service.JwtService;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = BookCopyController.class, excludeFilters = {
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { SecurityConfig.class, JwtAuthFilter.class })
})
@AutoConfigureMockMvc(addFilters = false)
class BookCopyControllerTest {
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private BookCopyService copyService;
  @MockBean
  JwtService jwtService;
  @MockBean
  CustomUserDetailsService customUserDetailsService;

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
  void getAllBookCopies_withFilters_ok() throws Exception {
    BookFormat format = BookFormat.HARDBACK;
    Double minPrice = 10.0;
    Double maxPrice = 20.0;
    when(copyService.findAll(bookId, format, minPrice, maxPrice)).thenReturn(List.of(bookCopy));
    mockMvc
        .perform(get("/books/" + bookId + "/copy")
            .param("format", format.name())
            .param("minPrice", minPrice.toString())
            .param("maxPrice", maxPrice.toString()))
        .andExpect(status().isOk());
  }

  @Test
  void getStock_withDate_ok() throws Exception {
    LocalDate date = LocalDate.now();
    when(copyService.calculateStock(copyId, date)).thenReturn(new BookStockResponse(copyId, 10));
    mockMvc
        .perform(get("/books/" + bookId + "/copy/" + copyId + "/stock")
            .param("t", date.toString()))
        .andExpect(status().isOk());
  }

  @Test
  void getAllBookCopies_ok() throws Exception {
    when(copyService.findAll(bookId, null, null, null)).thenReturn(List.of(bookCopy));
    mockMvc
        .perform(get("/books/" + bookId + "/copy"))
        .andExpect(status().isOk());
  }

  @Test
  void createBookCopy_ok() throws Exception {
    BookCopyCreationDto dto = new BookCopyCreationDto();
    when(copyService.save(any())).thenReturn(bookCopy);
    mockMvc
        .perform(post("/books/" + bookId + "/copy")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isCreated());
  }

  @Test
  void getStock_ok() throws Exception {
    when(copyService.calculateStock(copyId, null)).thenReturn(new BookStockResponse(copyId, 10));
    mockMvc
        .perform(get("/books/" + bookId + "/copy/" + copyId + "/stock"))
        .andExpect(status().isOk());
  }

  @Test
  void getBookCopyById_ok() throws Exception {
    when(copyService.getCopyById(copyId)).thenReturn(bookCopy);
    mockMvc
        .perform(get("/books/" + bookId + "/copy/" + copyId))
        .andExpect(status().isOk());
  }

  @Test
  void updateBookCopy_ok() throws Exception {
    BookCopyUpdate update = new BookCopyUpdate();
    when(copyService.updateBookCopy(org.mockito.ArgumentMatchers.eq(copyId), any())).thenReturn(bookCopy);
    mockMvc
        .perform(patch("/books/" + bookId + "/copy/" + copyId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(update)))
        .andExpect(status().isOk());
  }

  @Test
  void deleteBookCopy_ok() throws Exception {
    when(copyService.deleteBookCopy(copyId)).thenReturn(bookCopy);
    mockMvc
        .perform(delete("/books/" + bookId + "/copy/" + copyId))
        .andExpect(status().isOk());
  }

  private <T> T any() {
    return org.mockito.ArgumentMatchers.any();
  }
}
