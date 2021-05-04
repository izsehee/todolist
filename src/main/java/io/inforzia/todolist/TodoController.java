package io.inforzia.todolist;


import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class TodoController {
    private TodoItemRepository todoItemRepository;

    public TodoController(TodoItemRepository todoItemRepository) {
        this.todoItemRepository =  todoItemRepository;
    }

    @ApiOperation(value = "TODO 생성", notes = "해야할 일과 완료 여부를 작성하여 저장합니다.")
    @PostMapping(path = "/todo", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TodoItem> postTodo(@RequestHeader(value="authorization") String author, @RequestBody TodoItem todoItem) throws RestException{
        if(todoItem.getItem()==null||todoItem.getCompleted()==null)
            throw new RestException();
        todoItem.setUser(author);
        TodoItem savedTodo = todoItemRepository.save(todoItem);
        return ResponseEntity.ok(savedTodo);
    }

    @ApiOperation(value = "TODO 수정", notes = "해야할 일과 완료 여부를 수정하여 저장합니다.")
    @PutMapping(path = "/todo/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String putTodo(@PathVariable Integer id, @RequestBody TodoItem newTodoItem) throws RestException{
        if(!todoItemRepository.existsById(id))
            throw new RestException();
        todoItemRepository.findById(id)
                .map(todoItem -> {
                    todoItem.setItem(newTodoItem.getItem());
                    todoItem.setCompleted(newTodoItem.getCompleted());
                    return todoItemRepository.save(todoItem);
                })
                .orElseGet(() -> {
                    newTodoItem.
                            setId(id);
                    return todoItemRepository.save(newTodoItem);
                });
        return id + "번 TODO 수정";
    }

    @ApiOperation(value = "TODO의 완료여부 변경", notes = "해당 TODO의 완료 여부를 completed로 변경합니다.")
    @PatchMapping(path = "/todo/{id}/complete")
    public String patchTodoCompleted(@PathVariable(value = "id") Integer id) throws RestException{
        if(!todoItemRepository.existsById(id))
            throw new RestException();
        todoItemRepository.findById(id)
                .map(todoItem -> {
                    todoItem.setCompleted(true);
                    return todoItemRepository.save(todoItem);
                });
        return id + "번 TODO 수정";
    }

    @ApiOperation(value = "TODO의 완료여부 변경", notes = "해당 TODO의 완료 여부를 incompleted로 변경합니다.")
    @PatchMapping(path = "/todo/{id}/incomplete")
    public String patchTodoIncompleted(@PathVariable(value = "id") Integer id) throws RestException {
        if(!todoItemRepository.existsById(id))
            throw new RestException();
        todoItemRepository.findById(id)
                .map(todoItem -> {
                    todoItem.setCompleted(false);
                    return todoItemRepository.save(todoItem);
                });
        return id + "번 TODO 수정";
    }

    @ApiOperation(value = "TODO 삭제", notes = "해당 TODO를 삭제합니다.")
    @DeleteMapping(path = "/todo/{id}")
    public String deleteTodo(@PathVariable Integer id) throws RestException {
        if(!todoItemRepository.existsById(id))
            throw new RestException();
        todoItemRepository.deleteById(id);
        return id + "번 TODO 삭제";
    }

    @ApiOperation(value = "TODO 삭제", notes = "완료된 TODO를 모두 삭제합니다.")
    @DeleteMapping(path = "/todo/completed")
    public String deleteTodo() {
        val test = new String();
        todoItemRepository.findByCompleted(true).stream().forEach(item -> deleteTodo(item.getId()));
        return "완료된 TODO 삭제";
    }

    @ApiOperation(value = "TODO 조회", notes = "해당 TODO의 정보를 조회합니다.")
    @GetMapping(path = "/todo/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<TodoItem> getTodo(@PathVariable Integer id) throws RestException{
        if(!todoItemRepository.existsById(id))
            throw new RestException();
        return todoItemRepository.findById(id);
    }

    @ApiOperation(value = "TODO 조회", notes = "모든 TODO를 조회하거나 완료 여부로 조회합니다.")
    @GetMapping(path = "/todo", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TodoItem> getTodo(@RequestParam(value = "completed", required = false) Boolean isCompleted) {
        if(isCompleted==null)
            return todoItemRepository.findAll();
        else
            return todoItemRepository.findByCompleted(isCompleted);
    }
}
