package io.inforzia.todolist;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class TodoItemDAOImpl implements TodoItemDAO {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<TodoItem> findAll() {
        return em.createQuery("Select todo FROM TodoItem AS todo",
                TodoItem.class).getResultList();
    }

    @Override
    public List<TodoItem> findByCompleted(boolean isCompleted) {
        return em.createQuery("Select todo FROM TodoItem AS todo WHERE todo.completed = :completed",
                TodoItem.class)
                .setParameter("completed", isCompleted).getResultList();
    }

    @Override
    public TodoItem findById(int id) {
        return em.find(TodoItem.class, id);
    }

    @Override
    public void save(TodoItem todoItem) {
        em.persist(todoItem);
    }

    @Override
    public void deleteById(int id) {
//        if (em.find(TodoItem.class, id) == null) TODO 더 생각해보기
//            throw new NotFoundException();
        em.remove(em.find(TodoItem.class, id));
    }

//    @Override TODO 더 생각해보기
//    public void updateItem(int id, TodoItem todoItem) {
//        val todo = em.find(TodoItem.class, id);
//        todo.setItem(todoItem.getItem());
//        todo.setCompleted(todoItem.getCompleted());
//    }
//
//    @Override
//    public void updateCompleted(int id, boolean isCompleted) {
//        em.find(TodoItem.class, id).setCompleted(isCompleted);
//    }
}
