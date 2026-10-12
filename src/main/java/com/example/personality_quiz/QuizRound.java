package com.example.personality_quiz;

import java.io.Serializable;
import java.util.List;

/** 1回分のクイズ（最大16問）の進行状況。セッションに保存される。 */
public class QuizRound implements Serializable {
    private final String mode;
    private final String questionSet;
    private final List<QuizQuestion> questions;
    private int index = 0;
    private int score = 0;
    private boolean answered = false;

    public QuizRound(String mode, String questionSet, List<QuizQuestion> questions) {
        this.mode = mode;
        this.questionSet = questionSet;
        this.questions = questions;
    }

    public String getMode() { return mode; }
    public String getQuestionSet() { return questionSet; }
    public List<QuizQuestion> getQuestions() { return questions; }
    public int getIndex() { return index; }
    public int getScore() { return score; }
    public int getTotal() { return questions.size(); }
    public boolean isFinished() { return index >= questions.size(); }

    public QuizQuestion current() {
        return questions.get(index);
    }

    /** 現在の問題の回答を記録する。同じ問題を二重に数えない。 */
    public boolean recordAnswer(boolean correct) {
        if (answered || isFinished()) return false;
        answered = true;
        if (correct) score++;
        return true;
    }

    public void next() {
        if (!isFinished()) {
            index++;
            answered = false;
        }
    }
}
