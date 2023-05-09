package com.nufemit.repository;

import com.nufemit.model.Conversation;
import com.nufemit.model.Message;
import com.nufemit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    Optional<Message> findTop1ByConversationOrderByDateTimeDesc(Conversation conversation);

    List<Message> findByConversationOrderByDateTimeAsc(Conversation conversation);

    Optional<Message> findByIdAndSender(Long id, User user);

    List<Message> findByConversationAndUnreadAndSenderNot(Conversation conversation, Boolean unread, User user);

    void deleteAllByConversation(Conversation conversation);
}
