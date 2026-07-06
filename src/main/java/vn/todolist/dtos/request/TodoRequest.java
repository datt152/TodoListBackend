package vn.todolist.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TodoRequest {
    @NotBlank(message = "Title is required")
    private String title;
    private String description;
}