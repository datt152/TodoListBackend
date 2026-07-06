package vn.todolist.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Todo List API",
                version = "1.0.0",
                description = "Tài liệu API cho ứng dụng quản lý công việc"
        )
)
public class OpenApiConfig { }