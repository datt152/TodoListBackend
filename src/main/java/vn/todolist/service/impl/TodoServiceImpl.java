package vn.todolist.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.todolist.dto.request.TodoRequest;
import vn.todolist.entity.Todo;
import vn.todolist.exception.ResourceNotFoundException;
import vn.todolist.repository.TodoRepository;
import vn.todolist.service.TodoService;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;

    /**
     * Lấy danh sách công việc có hỗ trợ lọc và phân trang.
     *
     * @param search    Từ khóa tìm kiếm theo tiêu đề (có thể null hoặc rỗng).
     * @param completed Trạng thái hoàn thành (có thể null).
     * @param pageable  Đối tượng chứa thông tin phân trang (page, size, sort).
     * @return Trang (Page) chứa danh sách các công việc khớp với điều kiện.
     */
    @Override
    public Page<Todo> getTodos(String search, Boolean completed, Pageable pageable) {
        boolean hasSearch = (search != null && !search.trim().isEmpty());

        if (hasSearch && completed != null) {
            return todoRepository.findByTitleContainingIgnoreCaseAndCompleted(search, completed, pageable);
        } else if (hasSearch) {
            return todoRepository.findByTitleContainingIgnoreCase(search, pageable);
        } else if (completed != null) {
            return todoRepository.findByCompleted(completed, pageable);
        }
        return todoRepository.findAll(pageable);
    }
    /**
     * Tạo mới một công việc.
     *
     * @param request DTO chứa thông tin tiêu đề và mô tả của công việc.
     * @return Công việc vừa được lưu thành công vào cơ sở dữ liệu.
     */
    @Override
    public Todo createTodo(TodoRequest request) {
        Todo todo = Todo.builder()
                .title(request.getTitle().trim())
                .description(request.getDescription())
                .completed(false)
                .build();
        return todoRepository.save(todo);
    }
    /**
     * Cập nhật thông tin (tiêu đề, mô tả) của một công việc đã tồn tại.
     *
     * @param id      ID của công việc cần cập nhật.
     * @param request DTO chứa thông tin mới.
     * @return Công việc sau khi đã được cập nhật.
     * @throws ResourceNotFoundException nếu không tìm thấy công việc với ID tương ứng.
     */
    @Override
    public Todo updateTodo(Long id, TodoRequest request) {
        Todo todo = getTodoById(id);
        todo.setTitle(request.getTitle().trim());
        todo.setDescription(request.getDescription());
        return todoRepository.save(todo);
    }
    /**
     * Thay đổi trạng thái hoàn thành của công việc (Đã xong <-> Chưa xong).
     *
     * @param id ID của công việc cần đổi trạng thái.
     * @return Công việc sau khi đã đổi trạng thái.
     * @throws ResourceNotFoundException nếu không tìm thấy công việc với ID tương ứng.
     */
    @Override
    public Todo toggleStatus(Long id) {
        Todo todo = getTodoById(id);
        todo.setCompleted(!todo.isCompleted());
        return todoRepository.save(todo);
    }
    /**
     * Xóa một công việc khỏi hệ thống.
     *
     * @param id ID của công việc cần xóa.
     * @throws ResourceNotFoundException nếu không tìm thấy công việc với ID tương ứng.
     */
    @Override
    public void deleteTodo(Long id) {
        Todo todo = getTodoById(id);
        todoRepository.delete(todo);
    }
    /**
     * Hàm tiện ích nội bộ: Tìm công việc theo ID.
     *
     * @param id ID của công việc cần tìm.
     * @return Thực thể Todo tìm thấy trong cơ sở dữ liệu.
     * @throws ResourceNotFoundException nếu không tồn tại.
     */
    private Todo getTodoById(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found with id: " + id));
    }
}