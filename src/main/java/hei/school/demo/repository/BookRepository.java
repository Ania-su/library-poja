package hei.school.demo.repository;

import hei.school.demo.repository.model.JBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository
    extends JpaRepository<JBook, String>, JpaSpecificationExecutor<JBook> {}
