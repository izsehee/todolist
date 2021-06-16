package io.inforzia.todolist;

import java.util.List;

public interface TodoService {
    void saveTodo(TodoItem todo);
    void deleteTodo(int id);
    void deleteTodoCompleted();
    TodoItem getTodo(int id);
    List<TodoItem> getTodoList();
    List<TodoItem> getTodoList(boolean completed);
    void updateItem(int id, TodoItem todoItem);
    void updateCompleted(int id, boolean isCompleted);
}
