package vn.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.todolist.entity.Todo;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByTitleContainingIgnoreCase(String title);
    List<Todo> findByCompleted(boolean completed);
    List<Todo> findByTitleContainingIgnoreCaseAndCompleted(String title, boolean completed);
}