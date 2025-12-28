package com.example.userapi.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * ユーザー作成コマンド（単項目チェック用）
 * Java Recordを使用した不変DTO
 */
public record CreateUserCommand(

        @NotBlank(message = "名前は必須です")
        @Size(max = 100, message = "名前は100文字以内で入力してください")
        String name,

        @NotBlank(message = "メールアドレスは必須です")
        @Email(message = "正しいメールアドレス形式で入力してください")
        @Size(max = 255, message = "メールアドレスは255文字以内で入力してください")
        String email,

        @NotBlank(message = "パスワードは必須です")
        @Size(min = 8, max = 255, message = "パスワードは8文字以上255文字以内で入力してください")
        String password
) {
}