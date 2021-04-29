package io.inforzia.todolist;


import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class TodoController {
    private TodoItemRepository todoItemRepository;

    public TodoController(TodoItemRepository todoItemRepository) {
        this.todoItemRepository =  todoItemRepository;
    }

    @PostMapping(path = "/todo", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TodoItem> postTodo(@RequestBody TodoItem todoItem) {
        TodoItem savedTodo = todoItemRepository.save(todoItem);
        return ResponseEntity.ok(savedTodo);
    }

    @PutMapping(path = "/todo/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String putTodo(@PathVariable Integer id, @RequestBody TodoItem newTodoItem) {
        todoItemRepository.findById(id)
                .map(todoItem -> {
                    todoItem.setItem(newTodoItem.getItem());
                    todoItem.setCompleted(newTodoItem.getCompleted());
                    return todoItemRepository.save(todoItem);
                })
                .orElseGet(() -> {
                    newTodoItem.
                            setId(id);
                    return todoItemRepository.save(newTodoItem); //TODO 예외던지기
                });
        return id + "번 TODO 수정";
    }

    @PatchMapping(path = "/todo/{id}/complete")
    public String patchTodoCompleted(@PathVariable(value = "id") Integer id) {
        todoItemRepository.findById(id)
                .map(todoItem -> {
                    todoItem.setCompleted(true);
                    return todoItemRepository.save(todoItem);
                });
        return id + "번 TODO 수정";
    }

    @PatchMapping(path = "/todo/{id}/incomplete")
    public String patchTodoIncompleted(@PathVariable(value = "id") Integer id) {
        todoItemRepository.findById(id)
                .map(todoItem -> {
                    todoItem.setCompleted(false);
                    return todoItemRepository.save(todoItem);
                });
        return id + "번 TODO 수정";
    }

    @DeleteMapping(path = "/todo/{id}")
    public String deleteTodo(@PathVariable Integer id) {
        todoItemRepository.deleteById(id);
        return id + "번 TODO 삭제";
    }

    @DeleteMapping(path = "/todo/completed")
    public String deleteTodo() {
        val test = new String();
        todoItemRepository.findByCompleted(true).stream().forEach(item -> deleteTodo(item.getId()));
        return "완료된 TODO 삭제";
    }

    @GetMapping(path = "/todo/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public TodoItem getTodo(@PathVariable Integer id) {
        return todoItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("illegal argument :" + id));
    }

    @GetMapping(path = "/todo", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TodoItem> getTodo(@RequestParam(value = "completed", required = false) Boolean isCompleted) {
        if(isCompleted==null)
            return todoItemRepository.findAll();
        else
            return todoItemRepository.findByCompleted(isCompleted);
    }

}
