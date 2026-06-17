package hei.school.demo.repository;

import hei.school.demo.repository.model.JBookCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookCopyRepository
    extends JpaRepository<JBookCopy, String>, JpaSpecificationExecutor<JBookCopy> {}
