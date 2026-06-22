package hei.school.demo.repository;

import hei.school.demo.repository.model.JAuthor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AuthorRepository
    extends JpaRepository<JAuthor, String>, JpaSpecificationExecutor<JAuthor> {}
