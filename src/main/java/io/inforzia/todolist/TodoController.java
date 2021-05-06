package io.inforzia.todolist;


import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class TodoController {
    private TodoItemRepository todoItemRepository;

    public TodoController(TodoItemRepository todoItemRepository) {
        this.todoItemRepository =  todoItemRepository;
    }

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-todo");
    EntityManager em = emf.createEntityManager();
    @ApiOperation(value = "TODO 생성", notes = "해야할 일과 완료 여부를 작성하여 저장합니다.")
    @PostMapping(path = "/todo", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public TodoItem postTodo(@RequestHeader(value="authorization") String author, @RequestBody TodoItem todo) throws RestException{
        if(todo.getItem()==null||todo.getCompleted()==null)
            throw new RestException();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        TodoItem userTodo = new TodoItem();
        userTodo.setItem(todo.getItem());
        userTodo.setCompleted(todo.getCompleted());
        userTodo.setUser(author);
        em.persist(userTodo);

        tx.commit();
        return userTodo;
    }

    @ApiOperation(value = "TODO 수정", notes = "해야할 일과 완료 여부를 수정하여 저장합니다.")
    @PutMapping(path = "/todo/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String putTodo(@PathVariable Integer id, @RequestBody TodoItem newTodo) throws RestException{
        if(!todoItemRepository.existsById(id))
            throw new RestException();
        todoItemRepository.findById(id)
                .map(todoItem -> {
                    todoItem.setItem(newTodo.getItem());
                    todoItem.setCompleted(newTodo.getCompleted());
                    return todoItemRepository.save(todoItem);
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
        todoItemRepository.findById(id)
                .map(todoItem -> {
                    todoItem.setCompleted(false);
                    return todoItemRepository.save(todoItem);
                }).orElseThrow(() -> new RestException());
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
