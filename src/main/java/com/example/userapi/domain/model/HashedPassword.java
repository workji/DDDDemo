package com.example.userapi.domain.model;

import java.util.Objects;

/**
 * ハッシュ化済みパスワード値オブジェクト
 * パスワードは必ずハッシュ化された状態で保持
 */
public class HashedPassword {
    private final String hashedValue;

    public HashedPassword(String hashedValue) {
        if (hashedValue == null || hashedValue.isBlank()) {
            throw new IllegalArgumentException("パスワードは必須です");
        }
        this.hashedValue = hashedValue;
    }

    public String getHashedValue() {
        return hashedValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HashedPassword that = (HashedPassword) o;
        return Objects.equals(hashedValue, that.hashedValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hashedValue);
    }

    @Override
    public String toString() {
        return "HashedPassword{[PROTECTED]}";
    }
}