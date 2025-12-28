# User REST API - Clean Architecture with MyBatis

## プロジェクト概要

Clean Architecture / DDDの設計思想に基づき、MyBatisを使用したSpring Boot REST APIのサンプルプロジェクトです。

### 技術スタック

- **Java**: 17
- **Spring Boot**: 3.2.1
- **データベース**: MySQL 8.0
- **ビルドツール**: Maven
- **パスワード暗号化**: BCrypt
- **データアクセス**: MyBatis 3.0.3
- **その他**: Lombok不使用、Bean Validation使用

---

## プロジェクト構成

```
com.example.userapi
├─ presentation/                    # プレゼンテーション層
│  ├─ controller/                   # REST Controller
│  ├─ dto/                          # リクエスト/レスポンスDTO
│  └─ exception/                    # 例外ハンドラー
│
├─ application/                     # アプリケーション層
│  ├─ usecase/                      # ユースケース（業務フロー）
│  └─ dto/                          # コマンド/クエリDTO
│
├─ domain/                          # ドメイン層
│  ├─ model/                        # エンティティ、値オブジェクト
│  ├─ repository/                   # リポジトリインターフェース
│  └─ service/                      # ドメインサービス
│
└─ infrastructure/                  # インフラストラクチャ層
   ├─ persistence/
   │  ├─ entity/                    # DBエンティティ（MyBatis用）
   │  ├─ mapper/                    # MyBatisマッパー
   │  │  ├─ UserMapper.java         # マッパーインターフェース
   │  │  └─ UserEntityMapper.java   # ドメイン⇔DB変換
   │  └─ UserRepositoryImpl.java    # リポジトリ実装
   └─ security/                     # パスワードエンコーダー

resources/
└─ mapper/
   └─ UserMapper.xml                # MyBatis SQLマッピング
```

---

## MyBatisの設計ポイント

### 1. レイヤー分離の徹底

```
ドメイン層の User (不変・ビジネスルール)
         ↕ 変換 (UserEntityMapper)
インフラ層の UserEntity (可変・DB用)
         ↕ マッピング (MyBatis)
データベーステーブル
```

### 2. MyBatisマッパーの役割

| ファイル | 役割 |
|---------|------|
| `UserMapper.java` | SQL実行の命令書（インターフェース） |
| `UserMapper.xml` | 実際のSQL文を記述 |
| `UserEntity.java` | DBテーブルと1対1マッピング |
| `UserEntityMapper.java` | ドメイン⇔DB変換ロジック |

### 3. SQLインジェクション対策

```xml
<!-- ✅ 安全: #{} を使用（PreparedStatement） -->
<select id="selectById">
  SELECT * FROM users WHERE id = #{id}
</select>

<!-- ❌ 危険: ${} は使用しない（文字列置換） -->
<select id="selectById">
  SELECT * FROM users WHERE id = ${id}  <!-- SQLインジェクションリスク -->
</select>
```

---

## セットアップ手順

### 1. 前提条件

- Java 17以上がインストールされていること
- Maven 3.6以上がインストールされていること
- MySQL 8.0以上がインストールされていること

### 2. データベース準備

MySQLにログインしてデータベースとテーブルを作成します。

```sql
-- データベース作成
CREATE DATABASE userdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- データベース選択
USE userdb;

-- テーブル作成
CREATE TABLE users (
  id          BIGINT AUTO_INCREMENT PRIMARY KEY,
  name        VARCHAR(100) NOT NULL,
  email       VARCHAR(255) NOT NULL UNIQUE,
  password    VARCHAR(255) NOT NULL,
  created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 3. application.properties設定

`src/main/resources/application.properties`のDB接続情報を環境に合わせて修正してください。

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/userdb?useSSL=false&serverTimezone=Asia/Tokyo
spring.datasource.username=root
spring.datasource.password=your_password

# MyBatis設定
mybatis.mapper-locations=classpath:mapper/**/*.xml
mybatis.type-aliases-package=com.example.userapi.infrastructure.persistence.entity
mybatis.configuration.map-underscore-to-camel-case=true
```

### 4. MyBatis XMLマッピングファイル配置

`src/main/resources/mapper/UserMapper.xml`が正しく配置されていることを確認してください。

```
src/main/resources/
└─ mapper/
   └─ UserMapper.xml
```

### 5. ビルドと起動

```bash
# プロジェクトルートで実行
mvn clean install

# アプリケーション起動
mvn spring-boot:run
```

起動後、`http://localhost:8080`でアクセス可能になります。

---

## API仕様

### 1. ユーザー作成

**POST** `/api/users`

#### リクエスト例
```json
{
  "name": "山田太郎",
  "email": "taro.yamada@example.com",
  "password": "SecurePass123"
}
```

#### レスポンス例（201 Created）
```json
{
  "id": 1,
  "name": "山田太郎",
  "email": "taro.yamada@example.com",
  "createdAt": "2025-12-28 10:30:00",
  "updatedAt": "2025-12-28 10:30:00"
}
```

### 2. ユーザー取得

**GET** `/api/users/{id}`

#### レスポンス例（200 OK）
```json
{
  "id": 1,
  "name": "山田太郎",
  "email": "taro.yamada@example.com",
  "createdAt": "2025-12-28 10:30:00",
  "updatedAt": "2025-12-28 10:30:00"
}
```

### 3. ユーザー一覧取得

**GET** `/api/users`

#### レスポンス例（200 OK）
```json
[
  {
    "id": 1,
    "name": "山田太郎",
    "email": "taro.yamada@example.com",
    "createdAt": "2025-12-28 10:30:00",
    "updatedAt": "2025-12-28 10:30:00"
  }
]
```

### 4. ユーザー更新

**PUT** `/api/users/{id}`

#### リクエスト例（部分更新可能）
```json
{
  "name": "山田次郎"
}
```

#### レスポンス例（200 OK）
```json
{
  "id": 1,
  "name": "山田次郎",
  "email": "taro.yamada@example.com",
  "createdAt": "2025-12-28 10:30:00",
  "updatedAt": "2025-12-28 12:00:00"
}
```

### 5. ユーザー削除

**DELETE** `/api/users/{id}`

#### レスポンス（204 No Content）
レスポンスボディなし

---

## エラーレスポンス仕様

### バリデーションエラー（400 Bad Request）

```json
{
  "status": 400,
  "error": "Validation Error",
  "message": "入力内容に誤りがあります",
  "fieldErrors": [
    {
      "field": "email",
      "rejectedValue": "invalid-email",
      "message": "正しいメールアドレス形式で入力してください"
    }
  ],
  "timestamp": "2025-12-28 10:30:00"
}
```

### メール重複エラー（409 Conflict）

```json
{
  "status": 409,
  "error": "Duplicate Email",
  "message": "このメールアドレスは既に登録されています: test@example.com",
  "fieldErrors": [],
  "timestamp": "2025-12-28 10:30:00"
}
```

---

## バリデーション仕様

### 1. 単項目チェック

Presentation層のDTOで実施（Bean Validation）

- **name**: 必須、最大100文字
- **email**: 必須、メール形式、最大255文字
- **password**: 必須、8文字以上255文字以内

### 2. 相関チェック

Application層で実施

- 企業ドメイン（@company.com）の場合、名前に数字不可

### 3. 業務ロジックチェック

Domain層で実施

- メールアドレスの重複チェック（新規登録時）
- メールアドレスの他ユーザー使用チェック（更新時）

---

## MyBatis活用のベストプラクティス

### 1. 常に`#{}`を使用する

```xml
<!-- ✅ 推奨 -->
WHERE email = #{email}

<!-- ❌ 非推奨 -->
WHERE email = '${email}'
```

### 2. resultMapで明示的にマッピング

```xml
<resultMap id="userResultMap" type="UserEntity">
  <id property="id" column="id"/>
  <result property="createdAt" column="created_at"/>
</resultMap>
```

### 3. トランザクション管理はSpringに任せる

```java
@Service
@Transactional  // Spring管理のトランザクション
public class UserUseCase {
    // ...
}
```

---

## テスト用curlコマンド

### ユーザー作成
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"山田太郎","email":"taro@example.com","password":"password123"}'
```

### ユーザー取得
```bash
curl -X GET http://localhost:8080/api/users/1
```

### ユーザー一覧
```bash
curl -X GET http://localhost:8080/api/users
```

### ユーザー更新
```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"山田次郎"}'
```

### ユーザー削除
```bash
curl -X DELETE http://localhost:8080/api/users/1
```

---

## MyBatisログの確認

開発時にSQLログを確認するには、`application.properties`に以下を追加：

```properties
# MyBatisのSQLログ出力
logging.level.com.example.userapi.infrastructure.persistence.mapper=DEBUG
```

実行されるSQLが以下のように出力されます：

```
DEBUG c.e.u.i.p.m.UserMapper : ==>  Preparing: SELECT id, name, email, password, created_at, updated_at FROM users WHERE id = ?
DEBUG c.e.u.i.p.m.UserMapper : ==> Parameters: 1(Long)
DEBUG c.e.u.i.p.m.UserMapper : <==      Total: 1
```

---

## トラブルシューティング

### Q1. MyBatisマッパーが見つからない

**エラー**: `org.apache.ibatis.binding.BindingException: Invalid bound statement`

**対処法**:
1. `application.properties`の`mybatis.mapper-locations`を確認
2. XMLファイルのnamespace属性がマッパーインターフェースのFQCNと一致するか確認
3. `src/main/resources/mapper/`配下にXMLが配置されているか確認

### Q2. カラム名とプロパティ名のマッピングエラー

**対処法**:
```properties
# スネークケース→キャメルケース自動変換を有効化
mybatis.configuration.map-underscore-to-camel-case=true
```

### Q3. トランザクションがコミットされない

**対処法**:
- UseCaseクラスに`@Transactional`アノテーションを付与
- 例外が発生した場合は自動ロールバックされる

---

## 設計のポイント

### Clean Architectureの実現

1. **依存性の方向**: 外側→内側（Infrastructure → Application → Domain）
2. **ドメイン層の独立性**: MyBatisに依存しないビジネスロジック
3. **リポジトリパターン**: インターフェース（Domain）と実装（Infrastructure）の分離

### MyBatisとDDDの統合

1. **2層のエンティティ**:
    - `User`（ドメインモデル）: ビジネスルール保持、不変
    - `UserEntity`（DBモデル）: MyBatis用、可変

2. **変換層の導入**:
    - `UserEntityMapper`でドメイン⇔DB変換
    - 各層の責務を明確化

3. **SQLの明示化**:
    - XMLで明示的にSQL管理
    - 複雑なクエリも対応可能

---

## ライセンス

MIT License
