package project.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.domain.User;
import project.dto.AddUserRequest;
import project.service.AnalysisService;
import project.service.UserService;

import java.util.Map;

@RequiredArgsConstructor
@Controller
public class UserApiController {
    private final UserService userService;
    @Autowired
    private AnalysisService analysisService;

    @PostMapping("/api/user")
    public String signup(AddUserRequest request, @ModelAttribute User user,Model model){
       if (userService.isIdExists(user.getUserID())) {
            model.addAttribute("iderror", true);
            return "signup";
        }
        userService.save(request);
        return "redirect:/login";
    }
    @GetMapping("/api/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        new SecurityContextLogoutHandler().logout(request,response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login?logout";
    }
    @PostMapping("/api/analyze")
    public String analyzeText(@RequestParam("inputText") String inputText, Model model) {
        Map<String, Object> result = analysisService.analyzeText(inputText);

        model.addAllAttributes(result);

        return "result";
    }
}
