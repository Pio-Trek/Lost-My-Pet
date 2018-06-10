package graded.unit.lostmypetrestapi.repository;

import graded.unit.lostmypetrestapi.entity.messages.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * JPA repository that provides data access layer and query
 * methods for {@link Message} object
 *
 * @author Piotr Przechodzki
 * @since 21/05/2018
 */
public interface MessageRepository extends JpaRepository<Message, String> {

    List<Message> findAllBySenderId(String senderId);

    List<Message> findAllByRecipientId(String recipientId);

}
