package dev.article.article.Controller;

import dev.article.article.Entity.ArticleEntity;
import dev.article.article.Repository.ArticleEntityRepository;
import dev.article.article.Service.ArticleService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class adminInsert {
@Autowired
    private final ArticleService articleService;
    private final ArticleEntityRepository articleEntityRepository;
    public adminInsert(ArticleService articleService, ArticleEntityRepository articleEntityRepository){this.articleService = articleService; this.articleEntityRepository = articleEntityRepository;}


    @GetMapping("/adminInsert")
    public String insertArticle(HttpServletRequest request, Model model) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            int userAccessLvl = 0;
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userAccessLvl")) {

                    userAccessLvl = Integer.parseInt(cookie.getValue());

                    if (userAccessLvl == 3){

                        String userId = null;
                        for (Cookie c : cookies) {
                            if (c.getName().equals("userId")) {
                                userId = c.getValue();

                                List<ArticleEntity> articleList = articleService.getAllArticleById(userId);
                                model.addAttribute("articleList", articleList);
                                return "adminInsert";
                            }
                        }

                        return  "adminInsert";
                    } else if (userAccessLvl != 0) {
                        return "redirect:/homePage";
                    }
                }
            }
        }
        return "redirect:/";
    }

    @PostMapping("/adminAddArticle")
    public String uploadArticle(@RequestParam("filename") MultipartFile file, HttpServletRequest request, RedirectAttributes redirectAttributes){
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
        return "redirect:/adminInsert";
    }

    @PostMapping("/adminDownloadArticle")
    public ResponseEntity<ByteArrayResource> downloadArticle(@RequestParam("articleId") Long articleId){
        Optional<ArticleEntity> articleEntityOptional = articleEntityRepository.findById(articleId);
        if(articleEntityOptional.isPresent()) {
            ArticleEntity articleEntity = articleEntityOptional.get();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachement", articleEntity.getArticle_Name());

            ByteArrayResource resource = new ByteArrayResource(articleEntity.getArticle());

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        }else{
            return ResponseEntity.notFound().build();
        }

    }
}
