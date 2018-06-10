package graded.unit.lostmypetrestapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import graded.unit.lostmypetrestapi.LostMyPetRestApiApplication;
import graded.unit.lostmypetrestapi.entity.messages.Conversation;
import graded.unit.lostmypetrestapi.entity.messages.Message;
import graded.unit.lostmypetrestapi.entity.users.User;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*
 * INTEGRATION TEST FOR LOST MESSAGE CONTROLLER LAYER
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = LostMyPetRestApiApplication.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.JVM)
public class MessageControllerIntegrityTest {

    User sender = new User();
    User recipient = new User();
    User author = new User();

    Message message = new Message("Test subject", sender, recipient);

    Conversation conversation = new Conversation("Test conversation text", author, message);

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void shouldFetchMessageById() throws Exception {
        this.mockMvc.perform(get(MessageController.URI + "test-message-1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("test-message-1"))
                .andExpect(jsonPath("$.lastUpdate").exists())
                .andExpect(jsonPath("$.subject").exists())
                .andExpect(jsonPath("$.sender").exists())
                .andExpect(jsonPath("$.sender").isNotEmpty())
                .andExpect(jsonPath("$.recipient").exists())
                .andExpect(jsonPath("$.recipient").isNotEmpty())
                .andExpect(jsonPath("$.*", hasSize(5)))
                .andReturn();
    }

    @Test
    public void shouldVerifyInvalidFetchMessageById() throws Exception {
        this.mockMvc.perform(get(MessageController.URI + "fake-message")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Message with ID: 'fake-message' not found")))
                .andReturn();
    }

    @Test
    public void shouldFetchMessagesBySenderId() throws Exception {
        this.mockMvc.perform(get(MessageController.URI + "/sender/test-id-user2")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("test-message-1"))
                .andExpect(jsonPath("$[0].lastUpdate").exists())
                .andExpect(jsonPath("$[0].subject").exists())
                .andExpect(jsonPath("$[0].sender.id").value("test-id-user2"))
                .andExpect(jsonPath("$[0].sender").isNotEmpty())
                .andExpect(jsonPath("$[0].recipient").exists())
                .andExpect(jsonPath("$[0].recipient.id").value("test-id-user1"))
                .andExpect(jsonPath("$[0].recipient").isNotEmpty())
                .andExpect(jsonPath("$[0].*", hasSize(5)))
                .andReturn();
    }

    @Test
    public void shouldVerifyInvalidFetchMessagesBySenderId() throws Exception {
        this.mockMvc.perform(get(MessageController.URI + "/sender/fake-user")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("User with ID: 'fake-user' not found")))
                .andReturn();
    }

    @Test
    public void shouldFetchMessagesByRecipientId() throws Exception {
        this.mockMvc.perform(get(MessageController.URI + "/recipient/test-id-user1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("test-message-1"))
                .andExpect(jsonPath("$[0].lastUpdate").exists())
                .andExpect(jsonPath("$[0].subject").exists())
                .andExpect(jsonPath("$[0].sender.id").value("test-id-user2"))
                .andExpect(jsonPath("$[0].sender").isNotEmpty())
                .andExpect(jsonPath("$[0].recipient").exists())
                .andExpect(jsonPath("$[0].recipient.id").value("test-id-user1"))
                .andExpect(jsonPath("$[0].recipient").isNotEmpty())
                .andExpect(jsonPath("$[0].*", hasSize(5)))
                .andReturn();
    }

    @Test
    public void shouldVerifyInvalidFetchMessagesByRecipientId() throws Exception {
        this.mockMvc.perform(get(MessageController.URI + "/recipient/fake-user")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("User with ID: 'fake-user' not found")))
                .andReturn();
    }

    @Test
    public void shouldFetchConversationsByMessageId() throws Exception {
        this.mockMvc.perform(get(MessageController.URI + "/conversations/test-message-1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].date").exists())
                .andExpect(jsonPath("$[0].body").exists())
                .andExpect(jsonPath("$[0].author").exists())
                .andExpect(jsonPath("$[0].author").isNotEmpty())
                .andExpect(jsonPath("$[0].*", hasSize(5)))
                .andReturn();
    }

    @Test
    public void shouldVerifyInvalidFetchConversationsByMessageId() throws Exception {
        this.mockMvc.perform(get(MessageController.URI + "/conversations/fake-message")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Message with ID: 'fake-message' not found")))
                .andReturn();
    }

    @Test
    public void shouldCountConversationsByMessageId() throws Exception {
        this.mockMvc.perform(get(MessageController.URI + "/conversations/test-message-1/count")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("1")))
                .andReturn();
    }

    @Test
    public void shouldVerifyInvalidCountConversationsByMessageId() throws Exception {
        this.mockMvc.perform(get(MessageController.URI + "/conversations/fake-message/count")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Message with ID: 'fake-message' not found")))
                .andReturn();
    }

    @Test
    public void shouldInsertMessage() throws Exception {
        sender.setId("test-id-user3");
        sender.setEmail("user3@email.com");
        recipient.setId("test-id-user1");
        recipient.setEmail("user1@email.com");

        //convert Message object to JSON with Jackson
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(message);

        this.mockMvc.perform(post(MessageController.URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.lastUpdate").exists())
                .andExpect(jsonPath("$.subject").value("Test subject"))
                .andExpect(jsonPath("$.sender.id").value("test-id-user3"))
                .andExpect(jsonPath("$.sender").isNotEmpty())
                .andExpect(jsonPath("$.recipient").exists())
                .andExpect(jsonPath("$.recipient.id").value("test-id-user1"))
                .andExpect(jsonPath("$.recipient").isNotEmpty())
                .andExpect(jsonPath("$.*", hasSize(5)))
                .andReturn();
    }

    @Test
    public void shouldVerifyInvalidSenderInsertMessage() throws Exception {
        sender.setId("fake-user");
        sender.setEmail("fake-user@email.com");
        recipient.setId("test-id-user1");
        recipient.setEmail("user1@email.com");

        //convert Message object to JSON with Jackson
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(message);

        this.mockMvc.perform(post(MessageController.URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("User with email address: 'fake-user@email.com' not found")))
                .andReturn();
    }

    @Test
    public void shouldVerifyInvalidInsertMessage() throws Exception {
        sender.setEmail("fake-user@email.com");
        recipient.setId("test-id-user1");
        recipient.setEmail("user1@email.com");

        // Set empty subject
        message.setSubject("");

        //convert Message object to JSON with Jackson
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(message);

        this.mockMvc.perform(post(MessageController.URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Your request has issued a malformed or illegal request")))
                .andReturn();
    }

    @Test
    public void shouldInsertConversation() throws Exception {
        author.setEmail("user1@email.com");
        message.setId("test-message-1");

        //convert Message object to JSON with Jackson
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(conversation);

        this.mockMvc.perform(post(MessageController.URI + "/conversations")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.body").value("Test conversation text"))
                .andExpect(jsonPath("$.author.id").value("test-id-user1"))
                .andExpect(jsonPath("$.message.id").value("test-message-1"))
                .andExpect(jsonPath("$.*", hasSize(5)))
                .andReturn();
    }

    @Test
    public void shouldVerifyInvalidAuthorEmailWhenInsertConversation() throws Exception {
        author.setEmail("fake-user@email.com");
        message.setId("fake-user");

        //convert Message object to JSON with Jackson
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(conversation);

        this.mockMvc.perform(post(MessageController.URI + "/conversations")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("User with email address: 'fake-user@email.com' not found")))
                .andReturn();
    }

    @Test
    public void shouldVerifyInvalidInsertConversation() throws Exception {
        author.setEmail("user1@email.com");
        message.setId("test-message-1");

        // Se empty body text
        conversation.setBody("");

        //convert Message object to JSON with Jackson
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(conversation);

        this.mockMvc.perform(post(MessageController.URI + "/conversations")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Your request has issued a malformed or illegal request")))
                .andReturn();
    }

    @Test
    public void shouldRemoveMessage() throws Exception {
        this.mockMvc.perform(delete(MessageController.URI + "test-message-2")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Message deleted. (ID: test-message-2)")))
                .andReturn();
    }

    @Test
    public void shouldVerifyInvalidRemoveMessageId() throws Exception {
        this.mockMvc.perform(delete(MessageController.URI + "fake-message")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Message with ID: 'fake-message' not found.")))
                .andReturn();
    }
}