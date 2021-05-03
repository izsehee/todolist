package io.inforzia.todolist;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TodoItemRepository extends JpaRepository<TodoItem, Integer> {
    public List<TodoItem> findByCompleted(boolean isCompleted);
}

