package dev.article.article.Controller;

import dev.article.article.Service.ArticleService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class InsertArticle {

    @Autowired
    private ArticleService articleService;

    @PostMapping("/addArticle")
    public String uploadArticle(@RequestParam("filename")MultipartFile file, HttpServletRequest request, RedirectAttributes redirectAttributes){
        String userId = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userId")) {
                    userId = cookie.getValue();
                    break;
                }
            }
        }

        try{
            articleService.storeFile(file,userId);
            redirectAttributes.addFlashAttribute("successMessage", "文献上传中");
        } catch (IOException e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "文献上传失败");
        }
        return "redirect:/insertArticle";
    }
}
