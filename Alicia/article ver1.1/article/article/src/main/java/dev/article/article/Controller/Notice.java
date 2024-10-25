package dev.article.article.Controller;

import dev.article.article.Entity.NewsEntity;
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
public class Notice {

    private final NewsService newsService;

    public Notice(NewsService newsService) {
        this.newsService = newsService;
    }
    @GetMapping("/notice")
    public String notice(HttpServletRequest request, Model model){
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            int userAccessLvl = 0;
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userAccessLvl")) {

                    userAccessLvl = Integer.parseInt(cookie.getValue());

                    if (userAccessLvl == 3){

                        List<NewsEntity> newsList = newsService.getAllNews();
                        model.addAttribute("newsList",newsList);

                        return  "notice";
                    } else if (userAccessLvl != 0) {
                        return "redirect:/homePage";
                    }
                }
            }
        }
        return "redirect:/";}

    @PostMapping("/delectNews")
    public String deleteNews(@RequestParam("newsId") int newsId){
        newsService.deleteNewsById(newsId);
        return "redirect:/notice";
    }
    @PostMapping("/addNews")
    public String addNews(@RequestParam("newsNews") String newsNews){
        newsService.addNews(newsNews);
        return "redirect:/notice";
    }
}
