package io.inforzia.todolist;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TodoServiceImpl implements TodoService{

    @Autowired
    private TodoItemDAO todoItemDAO;

    public void saveTodo(TodoItem todo) {
        todoItemDAO.save(todo);
    }

    public void deleteTodo(int id) {
        todoItemDAO.deleteById(id);
    }

    public void deleteTodoCompleted() {
        todoItemDAO.findByCompleted(true).forEach(
                item -> todoItemDAO.deleteById(item.getId())
        );
    }

    public TodoItem getTodo(int id) {
        return todoItemDAO.findById(id);
    }

    public List<TodoItem> getTodoList() {
        return todoItemDAO.findAll();
    }

    public List<TodoItem> getTodoList(boolean completed) {
        return todoItemDAO.findByCompleted(completed);
    }

    public void updateItem(int id, TodoItem todoItem) {
        val todo = todoItemDAO.findById(id);
        todo.setItem(todoItem.getItem());
        todo.setCompleted(todoItem.getCompleted());
    }

    public void updateCompleted(int id, boolean isCompleted) {
        todoItemDAO.findById(id).setCompleted(isCompleted);
    }
}
