package com.nufemit.repository;

import com.nufemit.model.Conversation;
import com.nufemit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    Optional<Conversation> findByParticipant1AndParticipant2(User sender, User receiver);

    List<Conversation> findByParticipant1OrParticipant2(User user, User user1);
}
