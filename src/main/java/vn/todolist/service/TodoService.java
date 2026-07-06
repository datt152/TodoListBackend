package vn.todolist.service;

import vn.todolist.dto.request.TodoRequest;
import vn.todolist.entity.Todo;

public interface TodoService {
    Todo createTodo(TodoRequest request);
    Todo updateTodo(Long id, TodoRequest request);
    Todo toggleStatus(Long id);
    void deleteTodo(Long id);
}
