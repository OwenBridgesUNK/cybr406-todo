package com.cybr406.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
public class TodoRestController {
    @Autowired
    InMemoryTodoRepository inMemoryTodoRepository;

    @PostMapping("/todos")
    public ResponseEntity<Todo> createTodo(@Valid @RequestBody Todo toDoObject){
        Todo createdTodo = inMemoryTodoRepository.create(toDoObject);
        return new ResponseEntity<>(createdTodo, HttpStatus.CREATED);
    }

    @GetMapping("/todos/{id}")
    public ResponseEntity<Todo> getDetailInfo(@Valid @PathVariable long id){
        Optional<Todo> list = inMemoryTodoRepository.find(id);
        if (list.isPresent()) {
            Todo data = list.get();
            return new ResponseEntity<>(data, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/todos")
    public ResponseEntity<List<Todo>> paginated(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        List<Todo> paginatedList = inMemoryTodoRepository.findAll(page, size);
        return new ResponseEntity<>(paginatedList, HttpStatus.OK);
    }

    @PostMapping("/todos/{id}/tasks")
    public ResponseEntity<Todo> taskMethod(@PathVariable long id, @RequestBody Task task){
        Todo newTask = inMemoryTodoRepository.addTask(id,task);
        return new ResponseEntity<>(newTask, HttpStatus.CREATED);
    }

    @DeleteMapping("/todos/{id}")
    public ResponseEntity delete(@PathVariable long id){
        try {
            inMemoryTodoRepository.delete(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (NoSuchElementException error){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity deleteTask(@PathVariable long id){
        try {
            inMemoryTodoRepository.deleteTask(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (NoSuchElementException error){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
}
