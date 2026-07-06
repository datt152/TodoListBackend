package vn.todolist.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.todolist.dtos.request.TodoRequest;
import vn.todolist.dtos.response.PageResponse;
import vn.todolist.dtos.response.TodoResponse;
import vn.todolist.entities.Todo;
import vn.todolist.services.TodoService;
import vn.todolist.utils.TodoMapper;


import java.util.List;

@RestController
@RequestMapping("/api/v1/todos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Tạm thời mở CORS để Frontend React gọi API local không bị chặn
public class TodoController {

    private final TodoService todoService;

    /**
     * API Lấy danh sách công việc (có phân trang và tìm kiếm).
     *
     * @param search    Từ khóa tìm kiếm (tùy chọn).
     * @param completed Trạng thái hoàn thành (tùy chọn).
     * @param page      Trang hiện tại (mặc định là 0).
     * @param limit     Số lượng phần tử trên 1 trang (mặc định là 10).
     * @return Đối tượng PageResponse chứa danh sách TodoResponse.
     */
    @GetMapping
    public ResponseEntity<PageResponse<TodoResponse>> getAllTodos(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean completed,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {

        // Tạo cấu hình phân trang, ưu tiên task mới tạo lên đầu
        Pageable pageable = PageRequest.of(page, limit, Sort.by("createdAt").descending());

        Page<Todo> todoPage = todoService.getTodos(search, completed, pageable);

        // Map từ Entity sang DTO
        List<TodoResponse> content = todoPage.getContent().stream()
                .map(TodoMapper::toResponse)
                .toList();

        // Đóng gói vào PageResponse
        PageResponse<TodoResponse> response = PageResponse.<TodoResponse>builder()
                .content(content)
                .pageNo(todoPage.getNumber())
                .pageSize(todoPage.getSize())
                .totalElements(todoPage.getTotalElements())
                .totalPages(todoPage.getTotalPages())
                .isLast(todoPage.isLast())
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * API Tạo mới một công việc.
     *
     * @param request Payload chứa title và description (được validate bởi @Valid).
     * @return TodoResponse vừa được tạo với mã HTTP 201 (Created).
     */
    @PostMapping
    public ResponseEntity<TodoResponse> createTodo(@Valid @RequestBody TodoRequest request) {
        Todo savedTodo = todoService.createTodo(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(TodoMapper.toResponse(savedTodo));
    }

    /**
     * API Cập nhật toàn bộ thông tin công việc.
     *
     * @param id      ID công việc cần sửa.
     * @param request Payload chứa thông tin mới.
     * @return TodoResponse sau khi cập nhật.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TodoResponse> updateTodo(@PathVariable Long id, @Valid @RequestBody TodoRequest request) {
        Todo updatedTodo = todoService.updateTodo(id, request);
        return ResponseEntity.ok(TodoMapper.toResponse(updatedTodo));
    }

    /**
     * API Cập nhật nhanh trạng thái hoàn thành của công việc.
     *
     * @param id ID công việc cần đổi trạng thái.
     * @return TodoResponse sau khi đổi trạng thái.
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<TodoResponse> toggleStatus(@PathVariable Long id) {
        Todo toggledTodo = todoService.toggleStatus(id);
        return ResponseEntity.ok(TodoMapper.toResponse(toggledTodo));
    }

    /**
     * API Xóa công việc.
     *
     * @param id ID công việc cần xóa.
     * @return HTTP 204 (No Content) báo hiệu xóa thành công.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.noContent().build();
    }
}