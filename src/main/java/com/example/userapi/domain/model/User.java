package com.example.userapi.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * ユーザーエンティティ（ドメインモデル）
 * ビジネスルールとデータを持つ中心的な存在
 */
public class User {
    private final Long id;
    private final String name;
    private final Email email;
    private final HashedPassword password;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    // コンストラクタ（新規作成用）
    public User(String name, Email email, HashedPassword password) {
        this(null, name, email, password, null, null);
    }

    // コンストラクタ（DB取得用）
    public User(Long id, String name, Email email, HashedPassword password,
                LocalDateTime createdAt, LocalDateTime updatedAt) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("名前は必須です");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("名前は100文字以内で入力してください");
        }

        this.id = id;
        this.name = name;
        this.email = Objects.requireNonNull(email, "メールアドレスは必須です");
        this.password = Objects.requireNonNull(password, "パスワードは必須です");
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // 更新用のファクトリメソッド
    public User updateName(String newName) {
        return new User(this.id, newName, this.email, this.password,
                this.createdAt, LocalDateTime.now());
    }

    public User updateEmail(Email newEmail) {
        return new User(this.id, this.name, newEmail, this.password,
                this.createdAt, LocalDateTime.now());
    }

    public User updatePassword(HashedPassword newPassword) {
        return new User(this.id, this.name, this.email, newPassword,
                this.createdAt, LocalDateTime.now());
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Email getEmail() {
        return email;
    }

    public HashedPassword getPassword() {
        return password;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email=" + email +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}