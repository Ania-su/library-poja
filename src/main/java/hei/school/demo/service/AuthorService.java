package hei.school.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import hei.school.demo.entity.Author;
import hei.school.demo.repository.AuthorRepository;
import hei.school.demo.repository.mapper.AuthorMapper;
import hei.school.demo.repository.model.JAuthor;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public List<Author> findAll() {
        List<JAuthor> jAuthors = authorRepository.findAll();
        return authorMapper.toDomain(jAuthors);
    }
}
