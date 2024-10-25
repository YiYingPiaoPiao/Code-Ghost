package dev.article.article.Controller;

import dev.article.article.Entity.NewsEntity;
import dev.article.article.Entity.UserEntity;
import dev.article.article.Service.NewsService;
import dev.article.article.Service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class UserMange {

    private final UserService userService;
    public UserMange(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/userManage")
    public String userManage(HttpServletRequest request, Model model){
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            int userAccessLvl = 0;
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userAccessLvl")) {

                    userAccessLvl = Integer.parseInt(cookie.getValue());

                    if (userAccessLvl == 3){
                        List<UserEntity> usersList = userService.getAllUsers();
                        model.addAttribute("usersList",usersList);
                        return  "userManage";
                    } else if (userAccessLvl != 0) {
                        return "redirect:/homePage";
                    }
                }
            }
        }
        return "redirect:/";}

    @PostMapping("/selectUser")
    public String selectUser(@RequestParam("usersId") String usersId, @RequestParam("selectUserAccessLvl") int selectUserAccessLvl, @RequestParam("selectUserName") String selectUserName, Model model){
        model.addAttribute("selectUserId", usersId);
        model.addAttribute("selectUserAccessLvl", selectUserAccessLvl);
        model.addAttribute("selectUserName", selectUserName);
        return "userManage";
    }

    @PostMapping("/updateUser")
    public String updateUser(@RequestParam("selectUserId") String selectUserId,@RequestParam("selectUserAccess") int selectUserAccess, Model model){
        UserEntity user = userService.updateUserAccessLvl(selectUserId,selectUserAccess);

        return "redirect:/userManage";
    }
}
