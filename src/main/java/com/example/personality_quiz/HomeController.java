package com.example.personality_quiz;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    private final QuizService quizService;

    public HomeController(QuizService quizService) {
        this.quizService = quizService;
    }

    // トップ画面
    @GetMapping("/")
    public String home() {
        return "home";
    }

    // 出題方法を選ぶ画面
    @GetMapping("/select")
    public String select(
            @RequestParam String mode,
            @RequestParam(defaultValue = "false") boolean empty,
            HttpSession session,
            Model model) {

        @SuppressWarnings("unchecked")
        java.util.Set<String> wrong = (java.util.Set<String>) session.getAttribute("wrongKeys");
        if (wrong == null) wrong = new java.util.HashSet<>();
        model.addAttribute("wrongCount", quizService.countWrong(mode, wrong));
        model.addAttribute("empty", empty);

        model.addAttribute("mode", mode);

        String modeName = switch (mode) {
            case "meaning" -> "アルファベットの意味";
            case "en-to-ja" -> "アルファベットから日本語";
            case "ja-to-en" -> "日本語からアルファベット";
            default -> "学習モード";
        };

        model.addAttribute("modeName", modeName);

        return "select";
    }
}