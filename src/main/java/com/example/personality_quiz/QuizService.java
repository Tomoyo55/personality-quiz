package com.example.personality_quiz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class QuizService {

    public static final int ROUND_SIZE = 16;

    /** タイプ → 日本語名（16タイプ） */
    private static final Map<String, String> TYPES = new LinkedHashMap<>();
    static {
        TYPES.put("INTJ", "建築家");
        TYPES.put("INTP", "論理学者");
        TYPES.put("ENTJ", "指揮官");
        TYPES.put("ENTP", "討論者");
        TYPES.put("INFJ", "提唱者");
        TYPES.put("INFP", "仲介者");
        TYPES.put("ENFJ", "主人公");
        TYPES.put("ENFP", "運動家");
        TYPES.put("ISTJ", "管理者");
        TYPES.put("ISFJ", "擁護者");
        TYPES.put("ESTJ", "幹部");
        TYPES.put("ESFJ", "領事官");
        TYPES.put("ISTP", "巨匠");
        TYPES.put("ISFP", "冒険家");
        TYPES.put("ESTP", "起業家");
        TYPES.put("ESFP", "エンターテイナー");
    }

    /** アルファベット1文字 → 意味（8文字） */
    private static final Map<String, String> LETTERS = new LinkedHashMap<>();
    static {
        LETTERS.put("E", "外向型");
        LETTERS.put("I", "内向型");
        LETTERS.put("S", "現実型");
        LETTERS.put("N", "直観型");
        LETTERS.put("T", "思考型");
        LETTERS.put("F", "感情型");
        LETTERS.put("J", "判断型");
        LETTERS.put("P", "知覚型");
    }

    /** そのモードの全問題（16問）を、選択肢もシャッフルして作る */
    public List<QuizQuestion> buildAll(String mode) {
        List<QuizQuestion> list = new ArrayList<>();

        switch (mode) {
            case "meaning" -> {
                for (var e : LETTERS.entrySet()) {
                    String explanation = "「" + e.getKey() + "」は「" + e.getValue() + "」を表します。";
                    list.add(make("meaning:L2M:" + e.getKey(),
                            e.getKey() + " は何を表す？",
                            e.getValue(), LETTERS.values(), explanation));
                    list.add(make("meaning:M2L:" + e.getKey(),
                            "「" + e.getValue() + "」を表すアルファベットはどれ？",
                            e.getKey(), LETTERS.keySet(), explanation));
                }
            }
            case "en-to-ja" -> {
                for (var e : TYPES.entrySet()) {
                    list.add(make("en-to-ja:" + e.getKey(),
                            e.getKey() + " の日本語の説明はどれ？",
                            e.getValue(), TYPES.values(), typeExplanation(e.getKey(), e.getValue())));
                }
            }
            default -> {
                for (var e : TYPES.entrySet()) {
                    list.add(make("ja-to-en:" + e.getKey(),
                            "「" + e.getValue() + "」のタイプはどれ？",
                            e.getKey(), TYPES.keySet(), typeExplanation(e.getKey(), e.getValue())));
                }
            }
        }
        return list;
    }

    /** 新しい1回分を作る。questionSet=wrong のときは間違えた問題だけ。 */
    public QuizRound newRound(String mode, String questionSet, Set<String> wrongKeys) {
        List<QuizQuestion> pool = buildAll(mode);
        if ("wrong".equals(questionSet)) {
            pool.removeIf(q -> !wrongKeys.contains(q.getKey()));
        }
        Collections.shuffle(pool);
        if (pool.size() > ROUND_SIZE) {
            pool = new ArrayList<>(pool.subList(0, ROUND_SIZE));
        }
        return new QuizRound(mode, questionSet, pool);
    }

    private QuizQuestion make(String key, String question, String answer,
                              java.util.Collection<String> all, String explanation) {
        List<String> others = new ArrayList<>(all);
        others.remove(answer);
        Collections.shuffle(others);
        List<String> choices = new ArrayList<>(others.subList(0, 3));
        choices.add(answer);
        Collections.shuffle(choices);
        return new QuizQuestion(key, question, answer,
                choices.toArray(new String[0]), explanation);
    }

    private String typeExplanation(String type, String name) {
        StringBuilder sb = new StringBuilder();
        sb.append(type).append("は「").append(name).append("」と呼ばれるタイプです。");
        sb.append("（");
        for (int i = 0; i < type.length(); i++) {
            String c = String.valueOf(type.charAt(i));
            if (i > 0) sb.append(" / ");
            sb.append(c).append("＝").append(LETTERS.get(c));
        }
        sb.append("）");
        return sb.toString();
    }

    public int countWrong(String mode, Set<String> wrongKeys) {
        return (int) buildAll(mode).stream()
                .filter(q -> wrongKeys.contains(q.getKey())).count();
    }
}
