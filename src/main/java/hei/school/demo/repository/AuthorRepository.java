package hei.school.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import hei.school.demo.repository.model.JAuthor;

public interface AuthorRepository 
    extends JpaRepository<JAuthor, String>, JpaSpecificationExecutor<JAuthor>{}
