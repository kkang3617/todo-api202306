package com.example.todo.todoapi.api;

import com.example.todo.todoapi.dto.request.TodoCreateRequestDTO;
import com.example.todo.todoapi.dto.request.TodoModifyRequestDTO;
import com.example.todo.todoapi.dto.response.TodoDetailResponseDTO;
import com.example.todo.todoapi.dto.response.TodoListResponseDTO;
import com.example.todo.todoapi.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/todos")
@CrossOrigin(origins = "http://localhost:3000")
public class TodoController {

    private final TodoService todoService;
    
    //할 일 등록 요청
    @PostMapping
    public ResponseEntity<?> createTodo(@Validated @RequestBody TodoCreateRequestDTO requestDTO
            , BindingResult result) {

        //검증값 하나
        if(result.hasErrors()) {
            log.warn("DTO 검증 에러 발생: {}", result.getFieldError());

            return ResponseEntity
                    .badRequest()
                    .body(result.getFieldError());

            // 검증할 값이 여러개 일때
//            List<FieldError> fieldErrors = result.getFieldErrors();
//            fieldErrors.forEach(err -> {
//                log.warn("invalid client data - {}", err.toString());
//            });
//            return ResponseEntity
//                    .badRequest()
//                    .body(fieldErrors);
        }

        if(requestDTO == null) {
            return ResponseEntity
                    .badRequest()
                    .body("할일 정보를 전달해 주세요.");
        }


        try {
            TodoListResponseDTO responseDTO = todoService.create(requestDTO);
            return ResponseEntity
                    .ok()
                    .body(responseDTO);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .internalServerError()
                    .body(TodoListResponseDTO.builder().error(e.getMessage()));
        }

    }
    
    //할 일 삭제 요청
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTodo(@PathVariable("id") String todoId) {
        log.info("/api/todos/{} DELETE request!", todoId);

        if(todoId == null || todoId.trim().equals("")) {
            return ResponseEntity
                    .badRequest()
                    .body(TodoListResponseDTO.builder().error("ID를 전달해 주세요."));
        }

        try {
            TodoListResponseDTO responseDTO = todoService.delete(todoId);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(TodoListResponseDTO.builder().error(e.getMessage()));
        }

    }

    
    //할 일 목록 요청
    @GetMapping
    public ResponseEntity<?> retrieveTodoList() {
        log.info("/api/todos GET request");
        TodoListResponseDTO responseDTO = todoService.retrieve();
        return ResponseEntity.ok().body(responseDTO);
    }

    
    //할 일 수정 요청
    @RequestMapping(method = {RequestMethod.PUT, RequestMethod.PATCH})
    public  ResponseEntity<?> updateTodo(
            @Validated @RequestBody TodoModifyRequestDTO requestDTO,
            BindingResult result,
            HttpServletRequest request) {

        if(result.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(result.getFieldError());
        }

        log.info("/api/todos {} request!", request.getMethod()); //무슨요청으로 들어왔는지 확인
        log.info("modifying dto: {}", requestDTO);


        try {
            TodoListResponseDTO responseDTO = todoService.update(requestDTO);
            return ResponseEntity.ok().body(responseDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError()
                    .body(TodoListResponseDTO.builder().error(e.getMessage()));
        }


    }


}
