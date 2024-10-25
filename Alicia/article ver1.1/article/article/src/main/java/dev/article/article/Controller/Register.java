package dev.article.article.Controller;

import dev.article.article.Entity.UserEntity;
import dev.article.article.Service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class Register {

    private final UserService userService;

    public Register(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/adminPage")
    public String adminPage(HttpServletRequest request, Model model){
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            int userAccessLvl = 0;
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userAccessLvl")) {

                    userAccessLvl = Integer.parseInt(cookie.getValue());

                    if (userAccessLvl == 3){
                        String generateId = userService.generateId();
                        model.addAttribute("generateId",generateId);
                        return  "adminPage";
                    } else if (userAccessLvl != 0) {
                        return "redirect:/homePage";
                    }
                }
            }
        }
        return "redirect:/";
    }

    @PostMapping("/adminPage")
    public String register(@RequestParam("uid") String uid,@RequestParam("uname") String uname,@RequestParam("upwd") String upwd,@RequestParam("uaccess") int uaccess , Model model){
        UserEntity user = userService.createUser(uname,upwd,uaccess,uid);

        String newGeneratedId = userService.generateId();
        model.addAttribute("generateId", newGeneratedId);
        return  "redirect:/adminPage";
    }

}
