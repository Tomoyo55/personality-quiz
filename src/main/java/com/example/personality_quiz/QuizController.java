package com.example.personality_quiz;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;

@Controller
public class QuizController {

    private static final String ROUND = "round";
    private static final String WRONG = "wrongKeys";

    private final QuizService service;

    public QuizController(QuizService service) {
        this.service = service;
    }

    @SuppressWarnings("unchecked")
    private Set<String> wrongKeys(HttpSession session) {
        Set<String> set = (Set<String>) session.getAttribute(WRONG);
        if (set == null) {
            set = new HashSet<>();
            session.setAttribute(WRONG, set);
        }
        return set;
    }

    /** 新しいクイズを始める（毎回ランダム・最大16問） */
    @GetMapping("/quiz/start")
    public String start(
            @RequestParam(defaultValue = "ja-to-en") String mode,
            @RequestParam(defaultValue = "all") String questionSet,
            HttpSession session) {

        QuizRound round = service.newRound(mode, questionSet, wrongKeys(session));
        if (round.getTotal() == 0) {
            return "redirect:/select?mode=" + mode + "&empty=true";
        }
        session.setAttribute(ROUND, round);
        return "redirect:/quiz";
    }

    @GetMapping("/quiz")
    public String quiz(HttpSession session, Model model) {
        QuizRound round = (QuizRound) session.getAttribute(ROUND);
        if (round == null) {
            return "redirect:/";
        }
        if (round.isFinished()) {
            return result(round, session, model);
        }

        model.addAttribute("mode", round.getMode());
        model.addAttribute("questionSet", round.getQuestionSet());
        model.addAttribute("question", round.current());
        model.addAttribute("index", round.getIndex());
        model.addAttribute("score", round.getScore());
        model.addAttribute("total", round.getTotal());
        return "quiz";
    }

    /** 回答を記録する。間違えた問題は覚え、正解したら「間違えた問題」から外す。 */
    @PostMapping("/quiz/answer")
    @ResponseBody
    public Map<String, Object> answer(@RequestParam boolean correct, HttpSession session) {
        QuizRound round = (QuizRound) session.getAttribute(ROUND);
        if (round != null && !round.isFinished()) {
            String key = round.current().getKey();
            if (round.recordAnswer(correct)) {
                if (correct) {
                    wrongKeys(session).remove(key);
                } else {
                    wrongKeys(session).add(key);
                }
            }
            return Map.of("score", round.getScore());
        }
        return Map.of("score", 0);
    }

    @GetMapping("/quiz/next")
    public String next(HttpSession session) {
        QuizRound round = (QuizRound) session.getAttribute(ROUND);
        if (round != null) {
            round.next();
        }
        return "redirect:/quiz";
    }

    private String result(QuizRound round, HttpSession session, Model model) {
        model.addAttribute("mode", round.getMode());
        model.addAttribute("questionSet", round.getQuestionSet());
        model.addAttribute("score", round.getScore());
        model.addAttribute("total", round.getTotal());
        model.addAttribute("wrongCount",
                service.countWrong(round.getMode(), wrongKeys(session)));
        return "result";
    }
}
