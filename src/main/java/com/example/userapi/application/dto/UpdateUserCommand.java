package com.example.userapi.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

/**
 * ユーザー更新コマンド
 * 更新の場合は全項目任意（部分更新可能）
 */
public record UpdateUserCommand(

        @Size(max = 100, message = "名前は100文字以内で入力してください")
        String name,

        @Email(message = "正しいメールアドレス形式で入力してください")
        @Size(max = 255, message = "メールアドレスは255文字以内で入力してください")
        String email,

        @Size(min = 8, max = 255, message = "パスワードは8文字以上255文字以内で入力してください")
        String password
) {
}