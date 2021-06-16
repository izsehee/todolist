package io.inforzia.todolist;

import java.util.List;

public interface TodoItemDAO {
    List<TodoItem> findAll();
    List<TodoItem> findByCompleted(boolean completed);
    TodoItem findById(int id);
    void save(TodoItem todoItem);
    void deleteById(int id);
}
