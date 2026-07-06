package vn.todolist.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.todolist.dtos.request.TodoRequest;
import vn.todolist.entities.Todo;

public interface TodoService {
    Page<Todo> getTodos(String search, Boolean completed, Pageable pageable);

    Todo createTodo(TodoRequest request);
    Todo updateTodo(Long id, TodoRequest request);
    Todo toggleStatus(Long id);
    void deleteTodo(Long id);
}
