package com.example.userapi.infrastructure.persistence.mapper;

import com.example.userapi.infrastructure.persistence.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * MyBatisマッパーインターフェース
 * XMLファイルとマッピングされる
 */
@Mapper
public interface UserMapper {

    /**
     * ユーザーを挿入する
     * @param userEntity 挿入するユーザー
     * @return 挿入件数
     */
    int insert(UserEntity userEntity);

    /**
     * IDでユーザーを検索する
     * @param id ユーザーID
     * @return ユーザーエンティティ
     */
    Optional<UserEntity> selectById(@Param("id") Long id);

    /**
     * メールアドレスでユーザーを検索する
     * @param email メールアドレス
     * @return ユーザーエンティティ
     */
    Optional<UserEntity> selectByEmail(@Param("email") String email);

    /**
     * 全ユーザーを取得する
     * @return ユーザーエンティティリスト
     */
    List<UserEntity> selectAll();

    /**
     * ユーザーを更新する
     * @param userEntity 更新するユーザー
     * @return 更新件数
     */
    int update(UserEntity userEntity);

    /**
     * ユーザーを削除する
     * @param id 削除するユーザーID
     * @return 削除件数
     */
    int deleteById(@Param("id") Long id);

    /**
     * メールアドレスの存在チェック
     * @param email メールアドレス
     * @return 存在件数
     */
    int countByEmail(@Param("email") String email);
}