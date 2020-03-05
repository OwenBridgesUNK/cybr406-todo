package com.cybr406.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    TaskJpaRepository taskJpaRepository;

    @Autowired
    TodoJpaRepository todoJpaRepository;

    @PostMapping("/todos")
    public ResponseEntity<Todo> createTodo(@Valid @RequestBody Todo toDoObject){
        Todo createdTodo = todoJpaRepository.save(toDoObject);
        return new ResponseEntity<>(createdTodo, HttpStatus.CREATED);
    }

    @GetMapping("/todos/{id}")
    public ResponseEntity<Todo> getDetailInfo(@Valid @PathVariable long id){
        Optional<Todo> list = todoJpaRepository.findById(id);
        if (list.isPresent()) {
            Todo data = list.get();
            return new ResponseEntity<>(data, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/todos")
    public Page<Todo> findAll(Pageable page) {
        return todoJpaRepository.findAll(page);
    }

    //help
    @PostMapping("/todos/{id}/tasks")
    public ResponseEntity<Todo> taskMethod(@PathVariable long id, @RequestBody Task task){
        Optional<Todo> addTask = todoJpaRepository.findById(id);

        if (addTask.isPresent()) {
            Todo todo = addTask.get();
            todo.getTasks().add(task);
            task.setTodo(todo);
            taskJpaRepository.save(task);
            return new ResponseEntity<>(todo, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/todos/{id}")
    public ResponseEntity delete(@PathVariable long id){
        if (todoJpaRepository.existsById(id)) {
            todoJpaRepository.deleteById(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
//        try {
//            inMemoryTodoRepository.delete(id);
//            return new ResponseEntity(HttpStatus.NO_CONTENT);
//        } catch (NoSuchElementException error){
//            return new ResponseEntity(HttpStatus.NOT_FOUND);
//        }
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity deleteTask(@PathVariable long id){
        if (taskJpaRepository.existsById(id)) {
            taskJpaRepository.deleteById(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

//        try {
//            inMemoryTodoRepository.deleteTask(id);
//            return new ResponseEntity(HttpStatus.NO_CONTENT);
//        } catch (NoSuchElementException error){
//            return new ResponseEntity(HttpStatus.NOT_FOUND);
//        }
    }
}
