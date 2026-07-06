package vn.todolist.utils;

import vn.todolist.dto.response.TodoResponse;
import vn.todolist.entity.Todo;

public class TodoMapper {
    // Private constructor để ngăn chặn việc khởi tạo object từ class này
    private TodoMapper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static TodoResponse toResponse(Todo todo) {
        if (todo == null) {
            return null;
        }
        return TodoResponse.builder()
                .id(todo.getId())
                .title(todo.getTitle())
                .description(todo.getDescription())
                .completed(todo.isCompleted())
                .createdAt(todo.getCreatedAt())
                .updatedAt(todo.getUpdatedAt())
                .build();
    }
}
