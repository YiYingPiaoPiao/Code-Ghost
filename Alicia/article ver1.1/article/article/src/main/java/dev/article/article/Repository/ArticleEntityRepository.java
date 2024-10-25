package dev.article.article.Repository;

import dev.article.article.Entity.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleEntityRepository extends JpaRepository<ArticleEntity,Long> {
    @Query("SELECT u FROM ArticleEntity u WHERE u.upload_User_Id = :upload_User_Id")
    List<ArticleEntity> findAllByUserId(String upload_User_Id);

    @Query("SELECT ae.article_Name FROM ArticleEntity ae WHERE ae.upload_User_Id = :userId ORDER BY ae.id DESC")
    List<String> findLastById(String userId);
//    @Query("SELECT a FROM ArticleEntity a WHERE a.upload_User_Id = ?1 ORDER BY a.upload_Date DESC")
//    List<ArticleEntity> getLastArticlesByUploadUserId(String uploadUserId);
    @Query("SELECT a FROM ArticleEntity a WHERE a.upload_User_Id = ?1 ORDER BY a.id DESC")
    List<ArticleEntity> getLastArticlesByUploadUserId(String uploadUserId);
}
