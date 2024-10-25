package dev.article.article.Repository;

import dev.article.article.Entity.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageEntityRepository extends JpaRepository<ChatMessageEntity,Long> {
    @Query("SELECT u FROM ChatMessageEntity u WHERE (u.from_user = :userId AND u.to_user = :selectUserId) OR (u.from_user = :selectUserId AND u.to_user = :userId)")
    List<ChatMessageEntity> findAllChatById(String selectUserId, String userId);
}
