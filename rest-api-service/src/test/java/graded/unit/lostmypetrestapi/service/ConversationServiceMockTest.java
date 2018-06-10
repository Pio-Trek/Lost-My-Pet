package graded.unit.lostmypetrestapi.service;

import graded.unit.lostmypetrestapi.entity.messages.Conversation;
import graded.unit.lostmypetrestapi.entity.messages.Message;
import graded.unit.lostmypetrestapi.entity.users.User;
import graded.unit.lostmypetrestapi.repository.ConversationRepository;
import graded.unit.lostmypetrestapi.service.impl.ConversationServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/*
 * MOCK UNIT TEST FOR CONVERSATION SERVICE LAYER
 */
public class ConversationServiceMockTest {

    @Mock
    private ConversationRepository repository;

    @InjectMocks
    private ConversationServiceImpl service;

    private Message message = new Message("Test subject", new User(), new User());

    private Conversation conversation = new Conversation("Test text", new User(), message);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        message.setId("test-message");
    }

    @Test
    public void testGetAllByMessageId() {
        //given
        given(repository.getAllByMessageId("test-message")).willReturn(Collections.singletonList(conversation));

        //calling method under the test
        List<Conversation> allByMessageId = service.getAllByMessageId("test-message");

        //assert respond has 1 objects
        assertThat(allByMessageId).hasSize(1);

        //assert fields
        assertConversationFields(allByMessageId.get(0));

        //verify that repository was called
        verify(repository, times(1)).getAllByMessageId("test-message");
    }


    @Test
    public void testAddConversation() {
        //given
        given(repository.save(conversation)).willReturn(conversation);

        //calling method under the test
        ResponseEntity<Conversation> response = service.addConversation(conversation);

        //assert that HTTP code is 200 and body is not null
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();

        //assert fields
        assertConversationFields(response.getBody());

        //verify that repository was called
        verify(repository, times(1)).save(conversation);
    }

    @Test
    public void testCountAllByMessageId() {
        //given
        given(repository.countAllByMessageId("test-message")).willReturn(1);

        //calling method under the test
        int countAllByMessageId = service.countAllByMessageId("test-message");

        //assert that HTTP code is 200 and body is not null
        assertThat(countAllByMessageId).isEqualTo(1);

        //verify that repository was called
        verify(repository, times(1)).countAllByMessageId("test-message");
    }

    private void assertConversationFields(Conversation conversation) {
        assertThat(conversation.getBody()).isEqualTo("Test text");
        assertThat(conversation.getMessage().getSubject()).isEqualTo("Test subject");
    }
}