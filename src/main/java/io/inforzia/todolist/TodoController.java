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

@RestController
@Slf4j
public class TodoController {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-todo");
    EntityManager em = emf.createEntityManager();

    @ApiOperation(value = "TODO 생성", notes = "해야할 일과 완료 여부를 작성하여 저장합니다.")
    @PostMapping(path = "/todo", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public TodoItem postTodo(@RequestHeader(value="authorization") String author, @RequestBody TodoItem todo){
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
    @PutMapping(path = "/todo/{id}")
    public String putTodo(@PathVariable Integer id, @RequestBody TodoItem newTodo){
        if(em.find(TodoItem.class, id)==null)
            throw new RestException();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        em.find(TodoItem.class, id).setItem(newTodo.getItem());
        em.find(TodoItem.class, id).setCompleted(newTodo.getCompleted());

        tx.commit();
        return id + "번 TODO 수정";
    }

    @ApiOperation(value = "TODO의 완료여부 변경", notes = "해당 TODO의 완료 여부를 completed로 변경합니다.")
    @PatchMapping(path = "/todo/{id}/complete")
    public String patchTodoCompleted(@PathVariable(value = "id") Integer id){
        if(em.find(TodoItem.class, id)==null)
            throw new RestException();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        em.find(TodoItem.class, id).setCompleted(true);

        tx.commit();
        return id + "번 TODO 수정";
    }

    @ApiOperation(value = "TODO의 완료여부 변경", notes = "해당 TODO의 완료 여부를 incompleted로 변경합니다.")
    @PatchMapping(path = "/todo/{id}/incomplete")
    public String patchTodoIncompleted(@PathVariable(value = "id") Integer id){
        if(em.find(TodoItem.class, id)==null)
            throw new RestException();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        em.find(TodoItem.class, id).setCompleted(false);

        tx.commit();
        return id + "번 TODO 수정";
    }

    @ApiOperation(value = "TODO 삭제", notes = "해당 TODO를 삭제합니다.")
    @DeleteMapping(path = "/todo/{id}")
    public String deleteTodo(@PathVariable Integer id){
        if(em.find(TodoItem.class, id)==null)
            throw new RestException();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        em.remove(em.find(TodoItem.class, id));

        tx.commit();
        return id + "번 TODO 삭제";
    }

    @ApiOperation(value = "TODO 삭제", notes = "완료된 TODO를 모두 삭제합니다.")
    @DeleteMapping(path = "/todo/completed")
    public String deleteTodo() {
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        List<TodoItem> todoItemList = em.createQuery("Select todo FROM TodoItem AS todo WHERE todo.completed=true",
        TodoItem.class).getResultList();
        todoItemList.forEach(item -> em.remove(em.find(TodoItem.class, item.getId())));

        tx.commit();
        return "완료된 TODO 삭제";
    }

    @ApiOperation(value = "TODO 조회", notes = "해당 TODO의 정보를 조회합니다.")
    @GetMapping(path = "/todo/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public TodoItem getTodo(@PathVariable Integer id){
        if(em.find(TodoItem.class, id)==null)
            throw new RestException();
        return em.find(TodoItem.class, id);
    }

    @ApiOperation(value = "TODO 조회", notes = "모든 TODO를 조회하거나 완료 여부로 조회합니다.")
    @GetMapping(path = "/todo", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TodoItem> getTodo(@RequestParam(value = "completed", required = false) Boolean isCompleted) {
        if(isCompleted==null)
            return em.createQuery("Select todo FROM TodoItem AS todo",
                    TodoItem.class).getResultList();
        else
            return em.createQuery("Select todo FROM TodoItem AS todo WHERE todo.completed="+isCompleted,
                    TodoItem.class).getResultList();
    }
}
