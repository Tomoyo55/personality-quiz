package com.example.personality_quiz;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    // トップ画面
    @GetMapping("/")
    public String home() {
        return "home";
    }

    // 出題方法を選ぶ画面
    @GetMapping("/select")
    public String select(
            @RequestParam String mode,
            Model model) {

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