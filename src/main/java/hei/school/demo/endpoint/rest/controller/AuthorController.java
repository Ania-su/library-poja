package hei.school.demo.endpoint.rest.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hei.school.demo.endpoint.rest.controller.dto.AuthorRequest;

@RestController("/authors")
public class AuthorController {
    
    @GetMapping
    public ResponseEntity<?> getAllAuthors(){
        throw new RuntimeException("not implemented");
    }

    @PostMapping
    public ResponseEntity<?> createNewAuthors(@RequestBody AuthorRequest newAuthor){
        throw new RuntimeException("not implemented");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAuthorById(@RequestParam UUID id){
        throw new RuntimeException("not implemented");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAuthor(@RequestParam UUID id){
        throw new RuntimeException("not implemented");
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAuthors(@RequestParam UUID id){
        throw new RuntimeException("not implemented");
    }

}
