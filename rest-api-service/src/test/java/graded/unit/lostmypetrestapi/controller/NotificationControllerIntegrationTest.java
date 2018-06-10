package graded.unit.lostmypetrestapi.controller;

import graded.unit.lostmypetrestapi.LostMyPetRestApiApplication;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*
 * INTEGRATION TEST FOR USER NOTIFICATION CONTROLLER LAYER
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = LostMyPetRestApiApplication.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.JVM)
public class NotificationControllerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void shouldFetchUserNotificationsByUserId() throws Exception {
        this.mockMvc.perform(get(NotificationController.URI + "/user/test-id-user1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].user.id").exists())
                .andExpect(jsonPath("$[0].announcement.id").exists())
                .andExpect(jsonPath("$[0].*", hasSize(3)))
                .andReturn();
    }

    @Test
    public void shouldVerifyInvalidNotificationUserId() throws Exception {
        this.mockMvc.perform(get(NotificationController.URI + "/user/fake-id")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("User with ID: 'fake-id' not found"))
                .andReturn();
    }

    @Test
    public void shouldCountAnnouncementsByEnabledFalse() throws Exception {
        this.mockMvc.perform(get(NotificationController.URI + "/count/enabledFalse")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("1")))
                .andReturn();
    }

    @Test
    public void shouldRemoveUserNotification() throws Exception {
        this.mockMvc.perform(delete(NotificationController.URI + "1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("User notification deleted. (ID: 1)")))
                .andReturn();
    }

    @Test
    public void shouldVerifyInvalidRemoveUserNotification() throws Exception {
        this.mockMvc.perform(delete(NotificationController.URI + "0")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Your request has issued a malformed or illegal request")))
                .andReturn();
    }
}