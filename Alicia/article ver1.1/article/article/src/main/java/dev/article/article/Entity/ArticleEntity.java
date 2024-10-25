package dev.article.article.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name="article")
@Getter
@Setter
public class ArticleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    @Lob
    private byte[] article;
    private String upload_User_Id;
    private LocalDate upload_Date;
    private String article_Name;


}
