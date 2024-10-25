package dev.article.article.Service;

import dev.article.article.Entity.NewsEntity;
import dev.article.article.Repository.NewsEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService {
    @Autowired
    private NewsEntityRepository newsEntityRepository;

    public List<NewsEntity> getAllNews(){
        return newsEntityRepository.findAll();
    }
    public void deleteNewsById(int newsId){
        newsEntityRepository.deleteById((long) newsId);
    }

    public NewsEntity addNews (String newsNews){
        NewsEntity newsEntity = new NewsEntity();
        newsEntity.setNews(newsNews);
        newsEntityRepository.save(newsEntity);
        return newsEntity;
    }
}
