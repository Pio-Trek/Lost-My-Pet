package graded.unit.lostmypetrestapi.service;

import graded.unit.lostmypetrestapi.entity.messages.Message;
import graded.unit.lostmypetrestapi.entity.users.User;
import graded.unit.lostmypetrestapi.exception.CustomResponse;
import graded.unit.lostmypetrestapi.repository.MessageRepository;
import graded.unit.lostmypetrestapi.service.impl.MessageServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/*
 * MOCK UNIT TEST FOR MESSAGE SERVICE LAYER
 */
public class MessageServiceMockTest {

    @Mock
    private MessageRepository repository;

    @InjectMocks
    private MessageServiceImpl service;

    private User sender = new User();
    private User recipient = new User();

    private Message message = new Message("Test subject", sender, recipient);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        message.setId("test-message");
        sender.setId("sender-id");
        recipient.setId("recipient-id");
    }

    @Test
    public void testGetAllMessagesBySenderId() {
        //given
        given(repository.findAllBySenderId("sender-id")).willReturn(Collections.singletonList(message));

        //calling method under the test
        List<Message> allMessagesBySenderId = service.getAllMessagesBySenderId(sender.getId());

        //assert respond has 1 objects
        assertThat(allMessagesBySenderId).hasSize(1);

        //assert fields
        assertMessageFields(allMessagesBySenderId.get(0));

        //verify that repository was called
        verify(repository, times(1)).findAllBySenderId("sender-id");
    }

    @Test
    public void testGetAllMessagesByRecipientId() {
        //given
        given(repository.findAllByRecipientId("recipient-id")).willReturn(Collections.singletonList(message));

        //calling method under the test
        List<Message> allMessagesByRecipientId = service.getAllMessagesByRecipientId(recipient.getId());

        //assert respond has 1 objects
        assertThat(allMessagesByRecipientId).hasSize(1);

        //assert fields
        assertMessageFields(allMessagesByRecipientId.get(0));

        //verify that repository was called
        verify(repository, times(1)).findAllByRecipientId("recipient-id");
    }

    @Test
    public void testGetMessageById() {
        //given
        given(repository.findById("test-message")).willReturn(Optional.ofNullable(message));

        //calling method under the test
        Optional<Message> optMessage = service.getMessageById("test-message");

        //assert is present
        assertThat(optMessage.isPresent()).isTrue();

        //assert fields
        assertMessageFields(optMessage.orElseGet(null));

        //verify that repository was called
        verify(repository, times(1)).findById("test-message");
    }

    @Test
    public void testAddMessage() {
        //given
        given(repository.save(message)).willReturn(message);

        //calling method under the test
        ResponseEntity<Message> response = service.addMessage(message);

        //assert that HTTP code is 200 and body is not null
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();

        //assert fields
        assertMessageFields(response.getBody());

        //verify that repository was called
        verify(repository, times(1)).save(message);
    }

    @Test
    public void testDeleteMessage() {
        ResponseEntity<CustomResponse> response = service.deleteMessage("test-message");

        //assert that HTTP code is 200 and body is not null
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).contains("Message deleted. (ID: test-message)");

        //verify that repository was called
        verify(repository, times(1)).deleteById("test-message");
    }

    private void assertMessageFields(Message message) {
        assertThat(message.getId()).isInstanceOf(String.class);
        assertThat(message.getSubject()).isEqualTo("Test subject");
        assertThat(message.getSender()).isEqualTo(sender);
        assertThat(message.getRecipient()).isEqualTo(recipient);
    }
}