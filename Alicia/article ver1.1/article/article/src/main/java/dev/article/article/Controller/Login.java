package dev.article.article.Controller;

import dev.article.article.Entity.NewsEntity;
import dev.article.article.Entity.UserEntity;
import dev.article.article.Service.NewsService;
import dev.article.article.Service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;


import java.util.List;
import java.util.Optional;

@Controller
public class Login {

    private final UserService userService;
    private final NewsService newsService;
    public Login(UserService userService, NewsService newsService) {
        this.userService = userService;
        this.newsService = newsService;
    }

    @GetMapping("/")
    public String Index(HttpServletRequest request, Model model){

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            int userAccessLvl = 0;
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userAccessLvl")) {

                    userAccessLvl = Integer.parseInt(cookie.getValue());

                    if (userAccessLvl == 3){
                        return  "redirect:/adminPage";
                    } else if (userAccessLvl != 0){
                        return "redirect:/homePage";
                    }
                }
            }
        }
        List<NewsEntity> newsList = newsService.getAllNews();
        model.addAttribute("newsList",newsList);
        return "index";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response){
        Cookie userNameCookie = new Cookie("userName", null);
        Cookie userIdCookie = new Cookie("userId",null);
        Cookie userAccessLvlCookie = new Cookie("userAccessLvl",null);

        userIdCookie.setMaxAge(0);
        userNameCookie.setMaxAge(0);
        userAccessLvlCookie.setMaxAge(0);

        userNameCookie.setPath("/");
        userIdCookie.setPath("/");
        userAccessLvlCookie.setPath("/");

        response.addCookie(userIdCookie);
        response.addCookie(userNameCookie);
        response.addCookie(userAccessLvlCookie);

        return "redirect:/";
    }

    @PostMapping("/")
    public String login(@RequestParam("accID") String accountId,@RequestParam("pswd") String password, Model model, HttpServletResponse response){
        UserEntity user = userService.getUserByUserId(accountId);
        if (user != null && user.getUserPassword().equals(password)) {
            model.addAttribute("username", accountId);

            Cookie userNameCookie = new Cookie("userName", user.getUserName());
            Cookie userIdCookie = new Cookie("userId",accountId);
            Cookie userAccessLvlCookie = new Cookie("userAccessLvl",String.valueOf(user.getUserAccessLvl()));

            userNameCookie.setPath("/");
            userIdCookie.setPath("/");
            userAccessLvlCookie.setPath("/");

            response.addCookie(userIdCookie);
            response.addCookie(userNameCookie);
            response.addCookie(userAccessLvlCookie);

            if (user.getUserAccessLvl() == 3){
                return "redirect:/adminPage";
            }else {
                return "redirect:/homePage"; // Redirect to home page after successful login
            }
        } else {
            model.addAttribute("error", "账户ID或密码错误");
            return "index"; // Redirect back to the login page with error message
        }
    }

}
