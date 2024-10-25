package dev.article.article.Controller;

import dev.article.article.Entity.ChatMessageEntity;
import dev.article.article.Entity.UserEntity;
import dev.article.article.Service.ChatMessageService;
import dev.article.article.Service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class Message {
    private final UserService userService;
    private final ChatMessageService chatMessageService;
    public Message(UserService userService, ChatMessageService chatMessageService){this.userService = userService; this.chatMessageService = chatMessageService;}

    @GetMapping("/message")
    public String message(HttpServletRequest request, Model model, RedirectAttributes redirectAttributes){
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            int userAccessLvl = 0;
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userAccessLvl")) {

                    userAccessLvl = Integer.parseInt(cookie.getValue());

                    if (userAccessLvl == 3){
                        return  "redirect:/adminPage";
                    } else if (userAccessLvl != 0) {
                        String user = null;
                        List<UserEntity> usersList = userService.getAllUsers();
                        for (Cookie ck : cookies){
                            if (ck.getName().equals("userId")){
                                user = ck.getValue();
                                model.addAttribute("ckid",user);
                            }
                        }

                        model.addAttribute("usersList",usersList);
                        return "message";
                    }
                }
            }
        }
        return "redirect:/";
    }

    @PostMapping("/chatUser")
    public String SelectChatUser(RedirectAttributes redirectAttributes, Model model , @RequestParam("selectUsersId") String selectUsersId, @RequestParam("selectUserName") String selectUserName, @RequestParam("userId") String userId){

        List<ChatMessageEntity> chatList = chatMessageService.getAllChatById(selectUsersId, userId);
        redirectAttributes.addFlashAttribute("chatList",chatList);
        redirectAttributes.addFlashAttribute("toUserName",selectUserName);
        redirectAttributes.addFlashAttribute("toUserId", selectUsersId);
        redirectAttributes.addFlashAttribute("fromUserId", userId);
        return "redirect:/message";
    }

    @PostMapping("/sendMessage")
    public String sendMessage(RedirectAttributes redirectAttributes, Model model , @RequestParam("chat") String chat, @RequestParam("toUser") String toUser, @RequestParam("fromUser") String fromUser, @RequestParam("toUserName") String toUserName){
        if (toUser != "" && chat != ""){
            ChatMessageEntity chatMessage = chatMessageService.createChat(fromUser,toUser,chat);
        }
        List<ChatMessageEntity> chatList = chatMessageService.getAllChatById(toUser, fromUser);
        redirectAttributes.addFlashAttribute("chatList",chatList);
        redirectAttributes.addFlashAttribute("toUserName",toUserName);
        redirectAttributes.addFlashAttribute("toUserId", toUser);
        redirectAttributes.addFlashAttribute("fromUserId", fromUser);
        return "redirect:/message";
    }
}
