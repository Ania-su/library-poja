package hei.school.demo.entity;

import hei.school.demo.entity.enums.BookFormat;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Book {
  private String id;
  private String title;
  private String isbn;
  private LocalDate publicationYear;
  private Publisher publisher;
  private String description;
  private BookFormat format;
  private List<BookAuthor> bookAuthors;
  private List<BookGenre> genres;
  private List<BookCopy> copies;
}
