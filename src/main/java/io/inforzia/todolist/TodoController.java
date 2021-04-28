package io.inforzia.todolist;


import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class TodoController {
    @Autowired  //TODO 더 찾아보기
    private TodoItemRepository todoItemRepository;

    @PostMapping(path = "/todo", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveTodo(@RequestBody TodoItem todoItem) {
        TodoItem savedTodo = todoItemRepository.save(todoItem);
        return ResponseEntity.ok(savedTodo);
    }

    @PutMapping(path = "/todo/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateTodo(@PathVariable Integer id, @RequestBody TodoItem newTodoItem) {
        todoItemRepository.findById(id)
                .map(todoItem -> {
                    todoItem.setItem(newTodoItem.getItem());
                    return todoItemRepository.save(todoItem);
                })
                .orElseGet(() -> {
                    newTodoItem.
                            setId(id);
                    return todoItemRepository.save(newTodoItem);
                });
    }

    @PatchMapping(path = "/todo/{id}/complete")
    public void todoComplete(@PathVariable(value = "id") Integer id) {
        todoItemRepository.findById(id)
                .map(todoItem -> {
                    todoItem.setCompleted(true);
                    return todoItemRepository.save(todoItem);
                });
    }

    @PatchMapping(path = "/todo/{id}/incomplete")
    public void todoIncomplete(@PathVariable(value = "id") Integer id) {
        todoItemRepository.findById(id)
                .map(todoItem -> {
                    todoItem.setCompleted(false);
                    return todoItemRepository.save(todoItem);
                });
    }

    @DeleteMapping(path = "/todo/{id}")
    public void deleteTodo(@PathVariable Integer id) {
        todoItemRepository.deleteById(id);
    }

    @DeleteMapping(path = "/todo/completed")
    public void delCompletedTodo() {
        val test = new String();
        todoItemRepository.findByCompleted(true).stream().forEach(item -> deleteTodo(item.getId()));
    }

    @GetMapping(path = "/todo/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public TodoItem getTodo(@PathVariable Integer id) {
        return todoItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("illegal argument :" + id));
    }

    @GetMapping(path = "/todo", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TodoItem> all(@RequestParam(value = "completed") boolean isCompleted) {
        return todoItemRepository.findByCompleted(isCompleted);
    }

}
