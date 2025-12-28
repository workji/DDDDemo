package com.example.userapi.domain.repository;

import com.example.userapi.domain.model.Email;
import com.example.userapi.domain.model.User;

import java.util.List;
import java.util.Optional;

/**
 * ユーザーリポジトリインターフェース
 * ドメイン層で定義し、インフラ層で実装する（依存性逆転の原則）
 */
public interface UserRepository {

    /**
     * ユーザーを保存する
     * @param user 保存するユーザー
     * @return 保存されたユーザー（IDが採番される）
     */
    User save(User user);

    /**
     * IDでユーザーを検索する
     * @param id ユーザーID
     * @return ユーザー（存在しない場合はEmpty）
     */
    Optional<User> findById(Long id);

    /**
     * メールアドレスでユーザーを検索する
     * @param email メールアドレス
     * @return ユーザー（存在しない場合はEmpty）
     */
    Optional<User> findByEmail(Email email);

    /**
     * 全ユーザーを取得する
     * @return ユーザーリスト
     */
    List<User> findAll();

    /**
     * ユーザーを更新する
     * @param user 更新するユーザー
     * @return 更新されたユーザー
     */
    User update(User user);

    /**
     * ユーザーを削除する
     * @param id 削除するユーザーID
     */
    void deleteById(Long id);

    /**
     * メールアドレスが存在するかチェックする
     * @param email メールアドレス
     * @return 存在する場合true
     */
    boolean existsByEmail(Email email);
}