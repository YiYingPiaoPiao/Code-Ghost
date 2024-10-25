package dev.article.article.Service;

import dev.article.article.Component.WebSocketServer;
import dev.article.article.Entity.ChatMessageEntity;
import dev.article.article.Repository.ChatMessageEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMessageService {
    @Autowired
    private final ChatMessageEntityRepository chatMessageEntityRepository;
    private final WebSocketServer webSocketServer;

    public ChatMessageService(ChatMessageEntityRepository chatMessageEntityRepository, WebSocketServer webSocketServer) {
        this.chatMessageEntityRepository = chatMessageEntityRepository;
        this.webSocketServer = webSocketServer;
    }

    public List<ChatMessageEntity> getAllChatById(String selectUserId, String userId) {
        return chatMessageEntityRepository.findAllChatById(selectUserId,userId);
    }

    public ChatMessageEntity createChat(String fromUser, String toUser, String chat) {

        ChatMessageEntity chatMessageEntity = new ChatMessageEntity();

        chatMessageEntity.setFrom_user(fromUser);
        chatMessageEntity.setTo_user(toUser);
        chatMessageEntity.setMessage(chat);

        chatMessageEntityRepository.save(chatMessageEntity);

        webSocketServer.broadcast("Database update detected!");

        return chatMessageEntity;
    }
}
