package com.example.userapi.domain.model;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * メールアドレス値オブジェクト
 * メールアドレスのビジネスルールをカプセル化
 */
public class Email {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private final String value;

    public Email(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("メールアドレスは必須です");
        }
        if (value.length() > 255) {
            throw new IllegalArgumentException("メールアドレスは255文字以内で入力してください");
        }
        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("正しいメールアドレス形式で入力してください");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(value, email.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}