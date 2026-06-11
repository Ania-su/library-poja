package hei.school.demo.repository;

import hei.school.demo.entity.BookCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookCopyRepository
    extends JpaRepository<BookCopy, String>, JpaSpecificationExecutor<BookCopy> {}
