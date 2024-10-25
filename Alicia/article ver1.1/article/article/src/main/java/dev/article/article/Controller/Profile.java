package dev.article.article.Controller;

import dev.article.article.Entity.UserEntity;
import dev.article.article.Service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.catalina.User;
import org.springframework.beans.StandardBeanInfoFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class Profile {

    private final UserService userService;
    public Profile(UserService userService){
        this.userService = userService;
    }
    @GetMapping("/profile")
    public String profile(HttpServletRequest request, Model model) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            int userAccessLvl = 0;
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userAccessLvl")) {

                    userAccessLvl = Integer.parseInt(cookie.getValue());

                    if (userAccessLvl == 3){
                        return  "redirect:/adminPage";
                    } else if (userAccessLvl != 0) {
                        String userId = null;
                        for (Cookie c : cookies){
                            if (c.getName().equals("userId")){
                                userId = c.getValue();
                                UserEntity  userEntity = userService.getUserByUserId(userId);
                                model.addAttribute("userId", userEntity.getUserId());
                                model.addAttribute("userName", userEntity.getUserName());
                                model.addAttribute("userPassword", userEntity.getUserPassword());
                                // Assuming user access level is also available in the user object
                                model.addAttribute("userAccessLevel", userEntity.getUserAccessLvl());
                                return "profile";
                            }
                        }

                    }
                }
            }
        }
        return "redirect:/";
    }

    @PostMapping("/editPassword")
    public String editPassword(@RequestParam("npwd") String npwd, @RequestParam("rpwd")String rpwd, @RequestParam("uid") String uid, Model model, RedirectAttributes redirectAttributes){
        if (npwd.equals(rpwd)){
            UserEntity user = userService.updateUserPassword(npwd, uid);
            redirectAttributes.addFlashAttribute("successMessage", "密码更新成功");
            return "redirect:/profile";
        }else {
            model.addAttribute("error","密码不一致");
            return "profile";
        }
    }
}
