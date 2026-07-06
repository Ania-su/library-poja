package hei.school.demo.service;

import hei.school.demo.endpoint.rest.controller.dto.GenreProfit;
import hei.school.demo.repository.GenreRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GenreService {

  private final GenreRepository genreRepository;

  public List<GenreProfit> findGenreProfit() {

    List<GenreProfit> profits = new ArrayList<>();

    for (Object[] row : genreRepository.getGenreProfits()) {
      String name = (String) row[0];
      Double profit = (Double) row[1];
      profits.add(new GenreProfit(name, profit));
    }
    ;

    return profits;
  }
}
