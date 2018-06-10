package graded.unit.lostmypetrestapi.repository;

import graded.unit.lostmypetrestapi.entity.messages.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * JPA repository that provides data access layer and query
 * methods for {@link Conversation} object
 *
 * @author Piotr Przechodzki
 * @since 21/05/2018
 */
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    List<Conversation> getAllByMessageId(String messageId);

    int countAllByMessageId(String messageId);
}
