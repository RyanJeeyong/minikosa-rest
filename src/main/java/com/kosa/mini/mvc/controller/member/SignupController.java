package com.kosa.mini.mvc.controller.member;

import com.kosa.mini.mvc.domain.member.SignupDTO;
import com.kosa.mini.mvc.exception.DuplicateEmailException;
import com.kosa.mini.mvc.exception.DuplicateNicknameException;
import com.kosa.mini.mvc.exception.SignupException;
import com.kosa.mini.mvc.service.member.SignUpService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class SignupController {

    @Autowired
    private SignUpService signUpService;

    @GetMapping("/signup")
    public String signupView(Model model, HttpSession session) {
        // 로그인 상태인지 확인
        Object loggedInUser = session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            // 이미 로그인된 사용자는 홈 페이지로 리다이렉트
            return "redirect:/home";
        }
        model.addAttribute("signupDTO", new SignupDTO());
        return "signup";
    }

    @PostMapping("/signup")
    public String sign(@Valid @ModelAttribute("signupDTO") SignupDTO dto, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("signupDTO", dto); // 오류 발생 시 signupDTO를 다시 모델에 추가
            return "signup";
        }

        try {
            boolean success = signUpService.signUp(dto);
            if(success) {
                return "redirect:/login";
            } else {
                model.addAttribute("errorMessage", "회원가입에 실패했습니다.");
                model.addAttribute("signupDTO", dto); // 실패 시에도 signupDTO 추가
                return "signup";
            }
        } catch (DuplicateEmailException | DuplicateNicknameException | SignupException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("signupDTO", dto); // 예외 발생 시에도 signupDTO 추가
            return "signup";
        }
    }

    // 이메일 중복 검사 엔드포인트
    @GetMapping("/signup/check-email")
    @ResponseBody
    public ResponseEntity<?> checkEmail(@RequestParam("email") String email) {
        boolean exists = signUpService.isEmailExists(email);
        return ResponseEntity.ok().body("{\"exists\": " + exists + "}");
    }

    // 닉네임 중복 검사 엔드포인트
    @GetMapping("/signup/check-nickname")
    @ResponseBody
    public ResponseEntity<?> checkNickname(@RequestParam("nickname") String nickname) {
        boolean exists = signUpService.isNicknameExists(nickname);
        return ResponseEntity.ok().body("{\"exists\": " + exists + "}");
    }
}
