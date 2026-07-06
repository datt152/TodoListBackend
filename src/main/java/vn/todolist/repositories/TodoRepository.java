package vn.todolist.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.todolist.entities.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    Page<Todo> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Todo> findByCompleted(boolean completed, Pageable pageable);
    Page<Todo> findByTitleContainingIgnoreCaseAndCompleted(String title, boolean completed, Pageable pageable);
}