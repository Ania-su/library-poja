package hei.school.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import hei.school.demo.repository.GenreRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GenreServiceTest {

  @Mock private GenreRepository genreRepository;

  @InjectMocks private GenreService genreService;

  private Object[] fantasyRow;
  private Object[] horrorRow;

  @BeforeEach
  void setUp() {
    fantasyRow = new Object[] {"Fantasy", 1500.0};
    horrorRow = new Object[] {"Horror", 300.0};
  }

  @Test
  void findGenreProfit_shouldReturnMappedProfits_withValidRows() {
    when(genreRepository.getGenreProfits()).thenReturn(List.of(fantasyRow, horrorRow));

    var result = genreService.findGenreProfit();

    assertThat(result).hasSize(2);
    assertThat(result.get(0).getName()).isEqualTo("Fantasy");
    assertThat(result.get(0).getProfit()).isEqualTo(1500.0);
    assertThat(result.get(1).getName()).isEqualTo("Horror");
    assertThat(result.get(1).getProfit()).isEqualTo(300.0);
    verify(genreRepository).getGenreProfits();
  }

  @Test
  void findGenreProfit_shouldReturnEmptyList_withNoRows() {
    when(genreRepository.getGenreProfits()).thenReturn(List.of());

    var result = genreService.findGenreProfit();

    assertThat(result).isEmpty();
  }

  @Test
  void findGenreProfit_shouldPropagateException_whenRepositoryFails() {
    when(genreRepository.getGenreProfits()).thenThrow(new RuntimeException("database unavailable"));

    assertThatThrownBy(() -> genreService.findGenreProfit())
        .isInstanceOf(RuntimeException.class)
        .hasMessage("database unavailable");
  }
}
