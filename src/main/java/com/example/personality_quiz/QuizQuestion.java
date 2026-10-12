package com.example.personality_quiz;

public class QuizQuestion implements java.io.Serializable {
    private final String key;
    private final String question;
    private final String answer;
    private final String[] choices;
    private final String explanation;

    public QuizQuestion(String key, String question, String answer,
                        String[] choices, String explanation) {
        this.key = key;
        this.question = question;
        this.answer = answer;
        this.choices = choices;
        this.explanation = explanation;
    }

    /** 間違えた問題を覚えておくための識別子（モードごとに固有） */
    public String getKey() {
        return key;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String[] getChoices() {
        return choices;
    }

    public String getExplanation() {
        return explanation;
    }
}
