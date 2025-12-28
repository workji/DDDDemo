package com.example.userapi.infrastructure.persistence;

import com.example.userapi.domain.model.Email;
import com.example.userapi.domain.model.User;
import com.example.userapi.domain.repository.UserRepository;
import com.example.userapi.infrastructure.persistence.entity.UserEntity;
import com.example.userapi.infrastructure.persistence.mapper.UserEntityMapper;
import com.example.userapi.infrastructure.persistence.mapper.UserMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * ユーザーリポジトリ実装（MyBatis版）
 * MyBatisマッパーを使用してデータベースにアクセス
 */
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserMapper userMapper;

    public UserRepositoryImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User save(User user) {
        UserEntity entity = UserEntityMapper.toEntity(user);

        int inserted = userMapper.insert(entity);
        if (inserted == 0) {
            throw new RuntimeException("ユーザーの保存に失敗しました");
        }

        // 自動採番されたIDを使って再取得
        return findById(entity.getId())
                .orElseThrow(() -> new RuntimeException("保存したユーザーの取得に失敗しました"));
    }

    @Override
    public Optional<User> findById(Long id) {
        return userMapper.selectById(id)
                .map(UserEntityMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return userMapper.selectByEmail(email.getValue())
                .map(UserEntityMapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return userMapper.selectAll().stream()
                .map(UserEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public User update(User user) {
        if (user.getId() == null) {
            throw new IllegalArgumentException("更新対象のユーザーIDが指定されていません");
        }

        UserEntity entity = UserEntityMapper.toEntity(user);

        int updated = userMapper.update(entity);
        if (updated == 0) {
            throw new RuntimeException("ユーザーが見つかりません: ID=" + user.getId());
        }

        return findById(user.getId())
                .orElseThrow(() -> new RuntimeException("更新したユーザーの取得に失敗しました"));
    }

    @Override
    public void deleteById(Long id) {
        int deleted = userMapper.deleteById(id);
        if (deleted == 0) {
            throw new RuntimeException("ユーザーが見つかりません: ID=" + id);
        }
    }

    @Override
    public boolean existsByEmail(Email email) {
        int count = userMapper.countByEmail(email.getValue());
        return count > 0;
    }
}