package com.example.userapi.presentation.controller;

import com.example.userapi.application.dto.CreateUserCommand;
import com.example.userapi.application.dto.UpdateUserCommand;
import com.example.userapi.application.dto.UserResponse;
import com.example.userapi.application.usecase.UserUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ユーザーコントローラー
 * REST APIのエンドポイントを提供
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserUseCase userUseCase;

    public UserController(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    /**
     * ユーザー作成
     * POST /api/users
     */
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserCommand command) {
        UserResponse response = userUseCase.createUser(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * ユーザー取得
     * GET /api/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        UserResponse response = userUseCase.getUser(id);
        return ResponseEntity.ok(response);
    }

    /**
     * ユーザー一覧取得
     * GET /api/users
     */
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> responses = userUseCase.getAllUsers();
        return ResponseEntity.ok(responses);
    }

    /**
     * ユーザー更新
     * PUT /api/users/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserCommand command) {
        UserResponse response = userUseCase.updateUser(id, command);
        return ResponseEntity.ok(response);
    }

    /**
     * ユーザー削除
     * DELETE /api/users/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userUseCase.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}