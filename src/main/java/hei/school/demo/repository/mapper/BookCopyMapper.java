package hei.school.demo.repository.mapper;

import hei.school.demo.entity.Book;
import hei.school.demo.entity.BookCopy;
import hei.school.demo.repository.model.JBook;
import hei.school.demo.repository.model.JBookCopy;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class BookCopyMapper {

  public BookCopy toDomain(JBookCopy jBookCopy) {
    if (jBookCopy == null) return null;
    BookCopy bookCopy = new BookCopy();
    bookCopy.setId(jBookCopy.getId());
    bookCopy.setFormat(jBookCopy.getFormat());
    bookCopy.setSellingPrice(jBookCopy.getSellingPrice());
    if (jBookCopy.getBook() != null) {
      Book book = new Book();
      book.setId(jBookCopy.getBook().getId());
      book.setTitle(jBookCopy.getBook().getTitle());
      book.setPublicationDate(jBookCopy.getBook().getPublicationDate());
      book.setDescription(jBookCopy.getBook().getDescription());
      bookCopy.setBook(book);
    }
    return bookCopy;
  }

  public List<BookCopy> toDomain(List<JBookCopy> jBookCopies) {
    if (jBookCopies == null) return null;
    List<BookCopy> bookCopies = new ArrayList<>();
    for (JBookCopy jbc : jBookCopies) {
      bookCopies.add(toDomain(jbc));
    }
    return bookCopies;
  }

  public JBookCopy toJpa(BookCopy bookCopy) {
    if (bookCopy == null) return null;
    JBookCopy jBookCopy = new JBookCopy();
    jBookCopy.setId(bookCopy.getId());
    jBookCopy.setFormat(bookCopy.getFormat());
    jBookCopy.setSellingPrice(bookCopy.getSellingPrice());
    if (bookCopy.getBook() != null) {
      JBook jBook = new JBook();
      jBook.setId(bookCopy.getBook().getId());
      jBookCopy.setBook(jBook);
    }
    return jBookCopy;
  }
}
