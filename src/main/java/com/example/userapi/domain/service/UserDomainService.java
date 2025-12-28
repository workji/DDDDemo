package com.example.userapi.domain.service;

import com.example.userapi.domain.model.Email;
import com.example.userapi.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

/**
 * ユーザードメインサービス
 * エンティティ単体では表現できないビジネスルールを実装
 */
@Service
public class UserDomainService {

    private final UserRepository userRepository;

    public UserDomainService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * メールアドレスの重複チェック（業務ロジックチェック）
     * @param email チェックするメールアドレス
     * @throws DuplicateEmailException メールアドレスが既に存在する場合
     */
    public void checkEmailDuplication(Email email) {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException(
                    "このメールアドレスは既に登録されています: " + email.getValue());
        }
    }

    /**
     * メールアドレス重複例外
     */
    public static class DuplicateEmailException extends RuntimeException {
        public DuplicateEmailException(String message) {
            super(message);
        }
    }
}