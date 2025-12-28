package com.example.userapi.infrastructure.security;

import com.example.userapi.domain.model.HashedPassword;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * パスワードエンコーダー
 * BCryptを使用してパスワードをハッシュ化
 */
@Component
public class PasswordEncoder {

    private final BCryptPasswordEncoder encoder;

    public PasswordEncoder() {
        this.encoder = new BCryptPasswordEncoder();
    }

    /**
     * 平文パスワードをハッシュ化する
     * @param rawPassword 平文パスワード
     * @return ハッシュ化されたパスワード
     */
    public HashedPassword encode(String rawPassword) {
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("パスワードは必須です");
        }
        if (rawPassword.length() < 8) {
            throw new IllegalArgumentException("パスワードは8文字以上で入力してください");
        }
        if (rawPassword.length() > 255) {
            throw new IllegalArgumentException("パスワードは255文字以内で入力してください");
        }

        String hashed = encoder.encode(rawPassword);
        return new HashedPassword(hashed);
    }

    /**
     * パスワードを検証する
     * @param rawPassword 平文パスワード
     * @param hashedPassword ハッシュ化されたパスワード
     * @return 一致する場合true
     */
    public boolean matches(String rawPassword, HashedPassword hashedPassword) {
        return encoder.matches(rawPassword, hashedPassword.getHashedValue());
    }
}