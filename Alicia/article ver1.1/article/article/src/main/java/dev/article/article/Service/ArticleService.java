package dev.article.article.Service;

import dev.article.article.Entity.ArticleEntity;
import dev.article.article.Repository.ArticleEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.*;

@Service
public class ArticleService {
    @Autowired
    private ArticleEntityRepository articleEntityRepository;

    public void storeFile(MultipartFile file, String userId) throws IOException{
        ArticleEntity articleEntity = new ArticleEntity();
        articleEntity.setArticle_Name(file.getOriginalFilename());
        articleEntity.setArticle(file.getBytes());
        articleEntity.setUpload_User_Id(userId);
        articleEntity.setUpload_Date(LocalDate.now());
        articleEntityRepository.save(articleEntity);
    }

    public List<ArticleEntity> getAllArticle() {
        return articleEntityRepository.findAll();
    }

    public List<ArticleEntity> getAllArticleById(String upload_User_Id) {
        System.out.print(upload_User_Id);
        return articleEntityRepository.findAllByUserId(upload_User_Id);
    }

    public String getLastArticleNameByUserId(String userId) {
        List<String> articles = articleEntityRepository.findLastById(userId);
        if (!articles.isEmpty()) {
            // Get the first article name from the list (assuming it's the last one due to ordering)
            return articles.get(0);
        } else {
            // Handle case where no articles are found
            return "请上传文献"; // or throw an exception or handle it according to your application logic
        }
    }

    // Method to read longblob data from the database based on the last uploaded article for a user
    public String readLongBlobDataForLastArticleByUploadUserId(String uploadUserId) throws IOException {
        // Retrieve the last uploaded article entity for the given user upload ID
        List<ArticleEntity> lastArticles = articleEntityRepository.getLastArticlesByUploadUserId(uploadUserId);
        if (lastArticles.isEmpty()) {
            return null;
        }

        // Get the last article from the list
        ArticleEntity lastArticle = lastArticles.get(0);

        // Get the longblob data as byte array
        byte[] longBlobData = lastArticle.getArticle();
        if (longBlobData == null) {
            return null;
        }

        // Convert byte array to input stream
        ByteArrayInputStream inputStream = new ByteArrayInputStream(longBlobData);

        // Create a reader to read the input stream
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

        // Read the characters from the input stream and construct a string
        StringBuilder stringBuilder = new StringBuilder();
        int character;
        while ((character = reader.read()) != -1) {
            stringBuilder.append((char) character);
        }

        // Close the reader
        reader.close();

        return stringBuilder.toString();

    }
}
