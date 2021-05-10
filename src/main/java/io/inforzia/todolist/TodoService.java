package io.inforzia.todolist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TodoService {
    @Autowired
    private TodoItemDAOImpl todoItemDAO;

    public void todoSave(TodoItem todo){
        todoItemDAO.save(todo);
    }
    public void todoDelete(int id){todoItemDAO.deleteById(id);}
    public void todoDeleteCompleted() {
        todoItemDAO.findByCompleted(true).forEach(
            item -> todoItemDAO.deleteById(item.getId())
    );}
    public TodoItem todoRead(int id){return todoItemDAO.findById(id);}
    public List<TodoItem> todoListRead(){
        return todoItemDAO.findAll();
    }
    public List<TodoItem> todoListRead(boolean completed){
        return todoItemDAO.findByCompleted(completed);
    }
    public void updateItem(int id, TodoItem todoItem){
        todoItemDAO.updateItem(id, todoItem);

    }
    public void updateCompleted(int id, boolean isCompleted){
        todoItemDAO.updateCompleted(id, isCompleted);
    }
}
