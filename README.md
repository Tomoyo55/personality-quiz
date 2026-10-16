# 🌈 personality-quiz

Java / Spring Boot で作った、**16Personalities（16タイプ性格診断）の用語を暗記するための学習クイズ Web アプリ**です。
E��I・S・N・T・F・J・P の意味や、INTJ＝建築家 のような16タイプの名前を、4択クイズで繰り返し覚えられます。

[![Open in GitHub Codespaces](https://github.com/codespaces/badge.svg)](https://codespaces.new/Tomoyo55/personality-quiz?quickstart=1)
[![Deploy to Render](https://render.com/images/deploy-to-render-button.svg)](https://render.com/deploy?repo=https://github.com/Tomoyo55/personality-quiz)

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.1-6DB33F)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F)

<!-- 公開URLができたら下の行のコメントを外して差し替えてください
👉 **Live Demo:** https://your-app-name.onrender.com
-->

---

## ✨ 特徴

- **3つの学習モード**
  | モード | 内容 | 例 |
  | --- | --- | --- |
  | MODE 1：アルファベットの意味 | 8文字の意味を覚える（文字→意味／意味→文字の両方向） | `E` は何を表す？ → 外向型 |
  | MODE 2：アルファベットから日本語 | 4文字のタイプ名から日本語名を答える | `INTJ` の日本語の説明は？ → 建築家 |
  | MODE 3：日本語からアルファベット | 日本語名から4文字のタイプを答える | 「建築家」のタイプはどれ？ → `INTJ` |
- **毎回ランダム出題**：問題の順番も選択肢の並びも毎回シャッフル（1回あたり最大16問）
- **間違えた問題だけ復習**：間違えた問題を記憶し、正解すると復習リストから外れる
- **即時フィードバック**：回答するとその場で正誤と解説（例：`I＝内向型 / N＝直観型 / ...`）を表示
- **進捗バー・スコア表示**、スマホでも見やすいレスポンシブ対応
- データベース不要。インストールしてすぐ動きます

---

## 🚀 ボタン一つで動作確認する

インストール不要で、ブラウザだけで試せます。

### ① GitHub Codespaces（おすすめ・GitHubアカウントがあればOK）

上の **「Open in GitHub Codespaces」** ボタンをクリックするだけです。

1. Codespace が起動し、Java 17 の環境が自動で用意されます
2. アプリが自動でビルド・起動します（初回は依存ライブラリのダウンロードに数分かかります）
3. 起動するとポート `8080` が自動で開き、ブラウザにクイズ画面が表示されます

> ポート画面が開かないときは、VS Code 下部の **「ポート」タブ** から `8080` の 🌐 アイコンをクリックしてください。
> Codespaces には無料枠があります。使い終わったら Codespace を停止・削除すると安心です。

### ② Render にデプロイして自分の URL で公開する

**「Deploy to Render」** ボタンをクリックし、Render にログインして「Apply」を押すと、`Dockerfile` と `render.yaml` をもとに自動でビルド・公開されます（無料プランで動作）。

> 無料プランは一定時間アクセスがないとスリープし、再開時の起動に少し時間がかかります。

---

## 💻 ローカルで動かす

### 必要なもの

- **JDK 17 以上**（Maven は同梱の Maven Wrapper が自動で取得するため、別途インストール不要）

### 手順

```bash
# 1. クローン
git clone https://github.com/Tomoyo55/personality-quiz.git
cd personality-quiz

# 2. 起動
./mvnw spring-boot:run        # Windows の場合: mvnw.cmd spring-boot:run
```

起動したら http://localhost:8080 をブラウザで開いてください。

### テスト・ビルド

```bash
./mvnw test                   # テスト実行
./mvnw package                # target/ に実行可能 jar を作成
java -jar target/personality-quiz-0.0.1-SNAPSHOT.jar
```

ポートを変えたいとき：

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=9090
```

### Docker で動かす

```bash
docker build -t personality-quiz .
docker run --rm -p 8080:8080 personality-quiz
```

---

## 🎮 使い方

1. トップ画面で学習モードを選ぶ
2. 「**全ての問題**」（ランダム16問）または「**間違えた問題**」（復習）を選ぶ
3. 4択から答えを選ぶと、正誤と解説が表示される
4. 「次の問題へ」で進み、最後に結果（正解数）が表示される
5. 結果画面から、間違えた問題の復習やもう一度の挑戦ができる

---

## 🗂 ディレクトリ構成

```
personality-quiz
├── pom.xml
├── Dockerfile / render.yaml        # デプロイ用
├── .devcontainer/devcontainer.json # Codespaces 用
└── src/main
    ├── java/com/example/personality_quiz
    │   ├── PersonalityQuizApplication.java  # エントリーポイント
    │   ├── HomeController.java              # トップ画面・出題方法の選択画面
    │   ├── QuizController.java              # クイズの開始・回答・次の問題・結果
    │   ├── QuizService.java                 # 問題データと出題ロジック
    │   ├── QuizRound.java                   # 1回分のクイズの進行状況
    │   └── QuizQuestion.java                # 1問分のデータ
    └── resources/templates
        ├── home.html      # トップ画面
        ├── select.html    # 出題方法の選択
        ├── quiz.html      # 出題画面
        └── result.html    # 結果画面
```

## 🔀 画面と URL

| URL | 説明 |
| --- | --- |
| `GET /` | トップ画面（モード選択） |
| `GET /select?mode=...` | 出題方法の選択（`meaning` / `en-to-ja` / `ja-to-en`） |
| `GET /quiz/start?mode=...&questionSet=...` | 新しいクイズを開始（`questionSet` は `all` / `wrong`） |
| `GET /quiz` | 現在の問題を表示（全問終了で結果画面） |
| `POST /quiz/answer?correct=...` | 回答を記録（正誤はサーバー側のセッションに保存） |
| `GET /quiz/next` | 次の問題へ進む |

## 🛠 技術スタック

- Java 17
- Spring Boot 4.1.1（Spring MVC）
- Thymeleaf（サーバーサイドレンダリング）
- Maven（Maven Wrapper 同梱）

## 📝 補足

- 進行状況や「間違えた問題」は **HTTP セッション（メモリ上）** に保存しています。ブラウザを閉じる・サーバーを再起動するとリセットされます。
- 問題データは `QuizService` 内にコードで定義しています。タイプ名や文字の意味を変更・追加する場合は、`TYPES` / `LETTERS` を編集してください。

## 📄 ライセンス

未設定です。公開する場合は `LICENSE` ファイルを追加してください。
