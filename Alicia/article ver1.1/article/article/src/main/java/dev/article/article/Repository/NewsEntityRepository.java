package dev.article.article.Repository;

import dev.article.article.Entity.NewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsEntityRepository extends JpaRepository<NewsEntity,Long> {
}
