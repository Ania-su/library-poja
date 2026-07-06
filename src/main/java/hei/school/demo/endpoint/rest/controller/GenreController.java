package hei.school.demo.endpoint.rest.controller;

import hei.school.demo.endpoint.rest.controller.dto.GenreProfit;
import hei.school.demo.service.GenreService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GenreController {
  private final GenreService genreService;

  @GetMapping("/genres/profits")
  public ResponseEntity<List<GenreProfit>> findGenreProfit() {
    return ResponseEntity.ok(genreService.findGenreProfit());
  }
}
