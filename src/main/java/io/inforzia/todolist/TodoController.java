package io.inforzia.todolist;


import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class TodoController {
    @Autowired
    private TodoService todoService;

    @ApiOperation(value = "TODO 생성", notes = "해야할 일과 완료 여부를 작성하여 저장합니다.")
    @PostMapping(path = "/todo", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public TodoItem postTodo(@RequestHeader(value="authorization") String author, @RequestBody TodoItem todo){
        if(todo.getItem()==null||todo.getCompleted()==null)
            throw new RestException();
        todo.setUser(author);
        todoService.todoSave(todo);
        return todo;
    }

    @ApiOperation(value = "TODO 수정", notes = "해야할 일과 완료 여부를 수정하여 저장합니다.")
    @PutMapping(path = "/todo/{id}")
    public String putTodo(@RequestHeader(value="authorization") String author, @PathVariable Integer id, @RequestBody TodoItem newTodo){
        todoService.updateItem(id, newTodo);
        return id + "번 TODO 수정";
    }

    @ApiOperation(value = "TODO의 완료여부 변경", notes = "해당 TODO의 완료 여부를 completed로 변경합니다.")
    @PatchMapping(path = "/todo/{id}/complete")
    public String patchTodoCompleted(@RequestHeader(value="authorization") String author, @PathVariable(value = "id") Integer id){
        todoService.updateCompleted(id, true);
        return id + "번 TODO 수정";
    }

    @ApiOperation(value = "TODO의 완료여부 변경", notes = "해당 TODO의 완료 여부를 incompleted로 변경합니다.")
    @PatchMapping(path = "/todo/{id}/incomplete")
    public String patchTodoIncompleted(@RequestHeader(value="authorization") String author, @PathVariable(value = "id") Integer id){
        todoService.updateCompleted(id, false);
        return id + "번 TODO 수정";
    }

    @ApiOperation(value = "TODO 삭제", notes = "해당 TODO를 삭제합니다.")
    @DeleteMapping(path = "/todo/{id}")
    public String deleteTodo(@RequestHeader(value="authorization") String author, @PathVariable Integer id){
        todoService.todoDelete(id);
        return id + "번 TODO 삭제";
    }

    @ApiOperation(value = "TODO 삭제", notes = "완료된 TODO를 모두 삭제합니다.")
    @DeleteMapping(path = "/todo/completed")
    public String deleteTodo(@RequestHeader(value="authorization") String author) {
        todoService.todoDeleteCompleted();
        return "완료된 TODO 삭제";
    }

    @ApiOperation(value = "TODO 조회", notes = "해당 TODO의 정보를 조회합니다.")
    @GetMapping(path = "/todo/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public TodoItem getTodo(@RequestHeader(value="authorization") String author, @PathVariable Integer id){
        return todoService.todoRead(id);
    }

    @ApiOperation(value = "TODO 조회", notes = "모든 TODO를 조회하거나 완료 여부로 조회합니다.")
    @GetMapping(path = "/todo", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TodoItem> getTodo(@RequestHeader(value="authorization") String author, @RequestParam(value = "completed", required = false) Boolean isCompleted) {
        if(isCompleted==null)
            return todoService.todoListRead();
        else
            return todoService.todoListRead(isCompleted);
    }
}
