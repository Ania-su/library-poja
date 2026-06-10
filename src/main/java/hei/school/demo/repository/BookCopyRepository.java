package hei.school.demo.repository;

import hei.school.demo.entity.BookCopy;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookCopyRepository
    extends JpaRepository<BookCopy, UUID>, JpaSpecificationExecutor<BookCopy> {}
