package hei.school.demo.endpoint.rest.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hei.school.demo.endpoint.rest.controller.dto.AuthorRequest;
import hei.school.demo.entity.Author;
import hei.school.demo.service.AuthorService;
import lombok.AllArgsConstructor;


@AllArgsConstructor
@RestController
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorService authorService;
    
    @GetMapping
    public ResponseEntity<?> getAllAuthors(){
        List<Author> authors = authorService.findAll();
        return ResponseEntity.ok(authors);
    }

    @PostMapping
    public ResponseEntity<?> createNewAuthors(@RequestBody AuthorRequest newAuthor){
        Author author = authorService.createAuthor(newAuthor);
        return ResponseEntity.ok(author);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAuthorById(@RequestParam UUID id){
        Author author = authorService.getAuthorById(id);
        return ResponseEntity.ok(author);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAuthor(@RequestParam UUID id, @RequestBody AuthorRequest updateAuthor){
        Author author = authorService.updateAuthor(id, updateAuthor);
        return ResponseEntity.ok(author);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAuthors(@RequestParam UUID id){
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }

}
