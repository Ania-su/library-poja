package hei.school.demo.endpoint.rest.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import hei.school.demo.endpoint.rest.controller.dto.GenreProfit;
import hei.school.demo.service.GenreService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = GenreController.class)
@AutoConfigureMockMvc(addFilters = false)
class GenreControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private GenreService genreService;

  @Test
  void findGenreProfit_ok_withResults() throws Exception {
    var fantasy = new GenreProfit("Fantasy", 1500.0);
    var horror = new GenreProfit("Horror", 300.0);
    when(genreService.findGenreProfit()).thenReturn(List.of(fantasy, horror));

    mockMvc
        .perform(get("/genres/profits"))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Fantasy"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].profit").value(1500.0))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Horror"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].profit").value(300.0));
  }

  @Test
  void findGenreProfit_ok_withEmptyResults() throws Exception {
    when(genreService.findGenreProfit()).thenReturn(List.of());

    mockMvc
        .perform(get("/genres/profits"))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(0));
  }

  @Test
  void findGenreProfit_shouldReturn500_whenServiceThrows() throws Exception {
    when(genreService.findGenreProfit()).thenThrow(new RuntimeException("database unavailable"));

    mockMvc
        .perform(get("/genres/profits"))
        .andExpect(status().isInternalServerError())
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(500))
        .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Internal Server Error"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("database unavailable"));
  }
}
