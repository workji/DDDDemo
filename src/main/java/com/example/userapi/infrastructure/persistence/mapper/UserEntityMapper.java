package com.example.userapi.infrastructure.persistence.mapper;

import com.example.userapi.domain.model.Email;
import com.example.userapi.domain.model.HashedPassword;
import com.example.userapi.domain.model.User;
import com.example.userapi.infrastructure.persistence.entity.UserEntity;

/**
 * ドメインモデルとDBエンティティの変換マッパー
 * Clean Architectureの境界を維持するための変換層
 */
public class UserEntityMapper {

    /**
     * DBエンティティからドメインモデルへ変換
     */
    public static User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        Email email = new Email(entity.getEmail());
        HashedPassword password = new HashedPassword(entity.getPassword());

        return new User(
                entity.getId(),
                entity.getName(),
                email,
                password,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    /**
     * ドメインモデルからDBエンティティへ変換
     */
    public static UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }

        return new UserEntity(
                user.getId(),
                user.getName(),
                user.getEmail().getValue(),
                user.getPassword().getHashedValue(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}