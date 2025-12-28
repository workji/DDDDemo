package com.example.userapi.application.usecase;

import com.example.userapi.application.dto.CreateUserCommand;
import com.example.userapi.application.dto.UpdateUserCommand;
import com.example.userapi.application.dto.UserResponse;
import com.example.userapi.domain.model.Email;
import com.example.userapi.domain.model.HashedPassword;
import com.example.userapi.domain.model.User;
import com.example.userapi.domain.repository.UserRepository;
import com.example.userapi.domain.service.UserDomainService;
import com.example.userapi.infrastructure.security.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ユーザーユースケース
 * アプリケーション層でトランザクション境界と業務フローを制御
 */
@Service
@Transactional
public class UserUseCase {

    private final UserRepository userRepository;
    private final UserDomainService userDomainService;
    private final PasswordEncoder passwordEncoder;

    public UserUseCase(UserRepository userRepository,
                       UserDomainService userDomainService,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userDomainService = userDomainService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * ユーザー作成
     */
    public UserResponse createUser(CreateUserCommand command) {
        // 相関チェック: 名前とメールの整合性
        validateNameAndEmail(command.name(), command.email());

        // ドメインオブジェクト生成（単項目チェックはここで実行される）
        Email email = new Email(command.email());
        HashedPassword hashedPassword = passwordEncoder.encode(command.password());

        // 業務ロジックチェック: メール重複チェック
        userDomainService.checkEmailDuplication(email);

        // エンティティ作成と保存
        User user = new User(command.name(), email, hashedPassword);
        User savedUser = userRepository.save(user);

        return UserResponse.from(savedUser);
    }

    /**
     * ユーザー取得
     */
    @Transactional(readOnly = true)
    public UserResponse getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("ユーザーが見つかりません: ID=" + id));
        return UserResponse.from(user);
    }

    /**
     * ユーザー一覧取得
     */
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * ユーザー更新
     */
    public UserResponse updateUser(Long id, UpdateUserCommand command) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("ユーザーが見つかりません: ID=" + id));

        // 相関チェック
        validateNameAndEmail(command.name(), command.email());

        User updatedUser = existingUser;

        // 名前更新
        if (command.name() != null && !command.name().equals(existingUser.getName())) {
            updatedUser = updatedUser.updateName(command.name());
        }

        // メール更新
        if (command.email() != null && !command.email().equals(existingUser.getEmail().getValue())) {
            Email newEmail = new Email(command.email());
            // 業務ロジックチェック: 自分以外で同じメールが使われていないか
            userRepository.findByEmail(newEmail).ifPresent(user -> {
                if (!user.getId().equals(id)) {
                    throw new UserDomainService.DuplicateEmailException(
                            "このメールアドレスは既に使用されています");
                }
            });
            updatedUser = updatedUser.updateEmail(newEmail);
        }

        // パスワード更新
        if (command.password() != null && !command.password().isBlank()) {
            HashedPassword newPassword = passwordEncoder.encode(command.password());
            updatedUser = updatedUser.updatePassword(newPassword);
        }

        User saved = userRepository.update(updatedUser);
        return UserResponse.from(saved);
    }

    /**
     * ユーザー削除
     */
    public void deleteUser(Long id) {
        if (!userRepository.findById(id).isPresent()) {
            throw new UserNotFoundException("ユーザーが見つかりません: ID=" + id);
        }
        userRepository.deleteById(id);
    }

    /**
     * 相関チェック: 名前とメールアドレスの整合性
     */
    private void validateNameAndEmail(String name, String email) {
        // サンプル実装: 企業ドメイン(@company.com)の場合、名前に数字不可
        if (email != null && email.endsWith("@company.com")) {
            if (name != null && name.matches(".*\\d.*")) {
                throw new InvalidNameFormatException(
                        "企業メールアドレスの場合、名前に数字を含めることはできません");
            }
        }
    }

    // カスタム例外
    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }

    public static class InvalidNameFormatException extends RuntimeException {
        public InvalidNameFormatException(String message) {
            super(message);
        }
    }
}