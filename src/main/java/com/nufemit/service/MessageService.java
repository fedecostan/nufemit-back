package com.nufemit.service;

import com.nufemit.model.Conversation;
import com.nufemit.model.Message;
import com.nufemit.model.User;
import com.nufemit.model.dto.ConversationDTO;
import com.nufemit.model.dto.MessageDTO;
import com.nufemit.model.dto.NewMessageDTO;
import com.nufemit.repository.ConversationRepository;
import com.nufemit.repository.MessageRepository;
import com.nufemit.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Service
@AllArgsConstructor
@Slf4j
public class MessageService {

    private MessageRepository messageRepository;
    private ConversationRepository conversationRepository;
    private UserRepository userRepository;

    public List<ConversationDTO> getConversations(User user) {
        return conversationRepository.findByParticipant1OrParticipant2(user, user).stream()
            .map(conversation -> messageRepository.findTop1ByConversationOrderByDateTimeDesc(conversation)
                .map(lastMessage -> ConversationDTO.builder()
                    .conversationId(conversation.getId())
                    .userId(conversation.getParticipant1().equals(user) ?
                        conversation.getParticipant2().getId() : conversation.getParticipant1().getId())
                    .conversationUser(conversation.getParticipant1().equals(user) ?
                        conversation.getParticipant2().getShortName() : conversation.getParticipant1().getShortName())
                    .lastMessage(lastMessage.getMessage())
                    .unread(lastMessage.getSender() != user && lastMessage.getUnread())
                    .date(lastMessage.getDateTime())
                    .build())
                .orElse(null))
            .collect(Collectors.toList()).stream()
            .sorted(Comparator.comparing(ConversationDTO::getDate).reversed())
            .collect(Collectors.toList());
    }

    public List<MessageDTO> getMessages(Long id, User user) {
        return conversationRepository.findById(id)
            .map(conversation -> getMessages(conversation, user))
            .orElseThrow(EntityNotFoundException::new);
    }

    public Boolean sendMessage(NewMessageDTO newMessageDTO, User user) {
        userRepository.findById(newMessageDTO.getReceiverId())
            .map(receiver -> verifyExistingConversation(newMessageDTO, user, receiver))
            .orElseThrow(EntityNotFoundException::new);
        return TRUE;
    }

    public Boolean deleteMessage(Long id, User user) {
        messageRepository.findByIdAndSender(id, user)
            .ifPresent(message -> messageRepository.delete(message));
        return TRUE;
    }

    public void deleteAllConversationsForUser(User user) {
        conversationRepository.findByParticipant1OrParticipant2(user, user)
            .forEach(conversation -> {
                messageRepository.deleteAllByConversation(conversation);
                conversationRepository.delete(conversation);
            });
    }

    private List<MessageDTO> getMessages(Conversation conversation, User user) {
        messageRepository.findByConversationAndUnreadAndSenderNot(conversation, TRUE, user)
            .forEach(this::markMessageAsRead);
        return messageRepository.findByConversationOrderByDateTimeAsc(conversation).stream()
            .map(message -> mapToMessageDTO(message, user))
            .collect(Collectors.toList());
    }

    private MessageDTO mapToMessageDTO(Message message, User user) {
        return MessageDTO.builder()
            .message(message.getMessage())
            .dateTime(message.getDateTime())
            .flow(message.getSender().equals(user) ? "O" : "I")
            .build();
    }

    private void markMessageAsRead(Message message) {
        message.setUnread(FALSE);
        messageRepository.save(message);
    }

    private Boolean verifyExistingConversation(NewMessageDTO newMessageDTO, User sender, User receiver) {
        return conversationRepository.findByParticipant1AndParticipant2(sender, receiver)
            .map(conversation -> createMessage(conversation, newMessageDTO, sender))
            .orElseGet(() -> conversationRepository.findByParticipant1AndParticipant2(receiver, sender)
                .map(conversation -> createMessage(conversation, newMessageDTO, sender))
                .orElseGet(() -> createMessage(conversationRepository.save(Conversation.builder()
                    .participant1(sender)
                    .participant2(receiver)
                    .build()), newMessageDTO, sender)));
    }

    private Boolean createMessage(Conversation conversation, NewMessageDTO newMessageDTO, User sender) {
        messageRepository.save(Message.builder()
            .message(newMessageDTO.getMessage())
            .conversation(conversation)
            .sender(sender)
            .dateTime(LocalDateTime.now())
            .unread(TRUE)
            .build());
        return TRUE;
    }
}
