package graded.unit.lostmypetrestapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import graded.unit.lostmypetrestapi.LostMyPetRestApiApplication;
import graded.unit.lostmypetrestapi.entity.locations.Location;
import graded.unit.lostmypetrestapi.entity.pets.*;
import graded.unit.lostmypetrestapi.entity.posts.Found;
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

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.AnyOf.anyOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*
 * INTEGRATION TEST FOR FOUND PET ANNOUNCEMENT CONTROLLER LAYER
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = LostMyPetRestApiApplication.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.JVM)
public class FoundPostControllerIntegrationTest {

    private MockMvc mockMvc;

    private User member = new User("test-id-user2", "password123", "user2@email.com", "Test2", "User2", new Location("West Vinewood"));

    private Dog dog = new Dog(null, PetGender.Unknown, 2, false, true, new byte[]{10, 20, 30}, "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", setColours(), new DogBreed("Chihuahua"));
    private Found announcement = new Found(new Location("Oreton"), member, dog, new Date());

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        announcement.setEnabled(false);
    }

    @Test
    public void shouldFetchFoundPosts() throws Exception {
        this.mockMvc.perform(get(FoundPostController.URI)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].postType", equalTo("found")))
                .andExpect(jsonPath("$[0].addedDate").exists())
                .andExpect(jsonPath("$[0].location.id").exists())
                .andExpect(jsonPath("$[0].location.name").exists())
                .andExpect(jsonPath("$[0].pet.type", anyOf(is("dog"), is("cat"))))
                .andExpect(jsonPath("$[0].foundDate").exists())
                .andExpect(jsonPath("$[0].enabled").exists())
                .andExpect(jsonPath("$[0].*", hasSize(8)))
                .andReturn();
    }

    @Test
    public void shouldFetchFoundPostsByMemberId() throws Exception {
        this.mockMvc.perform(get(FoundPostController.URI + "/member/test-id-user2")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].postType", equalTo("found")))
                .andExpect(jsonPath("$[0].addedDate").exists())
                .andExpect(jsonPath("$[0].location.id").exists())
                .andExpect(jsonPath("$[0].location.name").exists())
                .andExpect(jsonPath("$[0].pet.type", anyOf(is("dog"), is("cat"))))
                .andExpect(jsonPath("$[0].foundDate").exists())
                .andExpect(jsonPath("$[0].enabled").exists())
                .andExpect(jsonPath("$[0].*", hasSize(8)))
                .andReturn();
    }

    @Test
    public void shouldVerifyInvalidPostsByMemberId() throws Exception {
        this.mockMvc.perform(get(FoundPostController.URI + "/member/fake-user")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("User with ID: 'fake-user' not found"))
                .andReturn();
    }

    @Test
    public void shouldFetchFoundDogsPosts() throws Exception {
        this.mockMvc.perform(get(FoundPostController.URI + "dogs")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].postType", equalTo("found")))
                .andExpect(jsonPath("$[0].addedDate").exists())
                .andExpect(jsonPath("$[0].location.id").exists())
                .andExpect(jsonPath("$[0].location.name").exists())
                .andExpect(jsonPath("$[0].pet.type").value("dog"))
                .andExpect(jsonPath("$[0].foundDate").exists())
                .andExpect(jsonPath("$[0].enabled").exists())
                .andExpect(jsonPath("$[0].*", hasSize(8)))
                .andReturn();
    }

    @Test
    public void shouldFetchFoundCatsPosts() throws Exception {
        this.mockMvc.perform(get(FoundPostController.URI + "cats")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].postType", equalTo("found")))
                .andExpect(jsonPath("$[0].addedDate").exists())
                .andExpect(jsonPath("$[0].location.id").exists())
                .andExpect(jsonPath("$[0].location.name").exists())
                .andExpect(jsonPath("$[0].pet.type").value("cat"))
                .andExpect(jsonPath("$[0].foundDate").exists())
                .andExpect(jsonPath("$[0].enabled").exists())
                .andExpect(jsonPath("$[0].*", hasSize(8)))
                .andReturn();
    }

    @Test
    public void shouldFetchFoundPostById() throws Exception {
        this.mockMvc.perform(get(FoundPostController.URI + "4")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.postType", equalTo("found")))
                .andExpect(jsonPath("$.addedDate").exists())
                .andExpect(jsonPath("$.location.id").exists())
                .andExpect(jsonPath("$.location.name").exists())
                .andExpect(jsonPath("$.pet.type", anyOf(is("dog"), is("cat"))))
                .andExpect(jsonPath("$.foundDate").exists())
                .andExpect(jsonPath("$.enabled").exists())
                .andExpect(jsonPath("$.*", hasSize(8)))
                .andReturn();
    }

    @Test
    public void shouldVerifyInvalidFetchFoundPostById() throws Exception {
        this.mockMvc.perform(get(FoundPostController.URI + "999")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Found pet announcement with ID: '999' not found"))
                .andReturn();
    }

    @Test
    public void shouldVerifyInvalidFetchFoundPostArgument() throws Exception {
        this.mockMvc.perform(get(FoundPostController.URI + "abc")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Your request has issued a malformed or illegal request"))
                .andReturn();
    }

    @Test
    public void shouldInsertFoundPost() throws Exception {
        //convert found pet announcement object to JSON with Jackson
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(announcement);

        mockMvc.perform(post(FoundPostController.URI)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void shouldVerifyInvalidInsertLostPost() throws Exception {
        //convert lost pet announcement object to JSON with Jackson
        ObjectMapper mapper = new ObjectMapper();
        announcement.setFoundDate(null);
        String json = mapper.writeValueAsString(announcement);

        this.mockMvc.perform(post(FoundPostController.URI)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Your request has issued a malformed or illegal request"))
                .andReturn();
    }

    @Test
    public void shouldUpdateFoundPost() throws Exception {
        //assign ID that already exist in database
        announcement.setId(3L);
        announcement.getPet().setId(3L);

        //update (change) random details
        announcement.getPet().setAge(5);
        announcement.setFoundDate(new Date());

        //convert Admin object to JSON with Jackson
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(announcement);

        this.mockMvc.perform(put(FoundPostController.URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.postType", equalTo("found")))
                .andExpect(jsonPath("$.addedDate").exists())
                .andExpect(jsonPath("$.location.id").exists())
                .andExpect(jsonPath("$.location.name").exists())
                .andExpect(jsonPath("$.pet.type", anyOf(is("dog"), is("cat"))))
                .andExpect(jsonPath("$.pet.age").value(5))
                .andExpect(jsonPath("$.foundDate").exists())
                .andExpect(jsonPath("$.enabled").exists())
                .andExpect(jsonPath("$.*", hasSize(8)))
                .andReturn();
    }

    @Test
    public void shouldVerifyInvalidUpdateFoundPostId() throws Exception {
        announcement.setId(999L);

        //convert Admin object to JSON with Jackson
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(announcement);

        this.mockMvc.perform(put(FoundPostController.URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Found pet announcement with ID: '999' not found")))
                .andReturn();
    }

    @Test
    public void shouldVerifyInvalidUpdateFoundPost() throws Exception {
        announcement.setId(1L);

        //update (change) some details
        announcement.setFoundDate(null);

        //convert Admin object to JSON with Jackson
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(announcement);

        this.mockMvc.perform(put(FoundPostController.URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Your request has issued a malformed or illegal request")))
                .andReturn();
    }

    @Test
    public void shouldVerifyInvalidPetIdWhenUpdatePost() throws Exception{
        announcement.setId(3L);
        announcement.getPet().setId(999L);

        //convert Admin object to JSON with Jackson
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(announcement);

        this.mockMvc.perform(put(FoundPostController.URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Pet ID is not valid or found. Updating pet ID: 3. Existing pet ID: 999.")))
                .andReturn();
    }

    @Test
    public void shouldSetEnabledFoundPost() throws Exception {
        this.mockMvc.perform(get(FoundPostController.URI + "3")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.postType", equalTo("found")))
                .andExpect(jsonPath("$.addedDate").exists())
                .andExpect(jsonPath("$.location.id").exists())
                .andExpect(jsonPath("$.location.name").exists())
                .andExpect(jsonPath("$.pet.type", anyOf(is("dog"), is("cat"))))
                .andExpect(jsonPath("$.pet.age").value(5))
                .andExpect(jsonPath("$.foundDate").exists())
                .andExpect(jsonPath("$.enabled").value(false))
                .andExpect(jsonPath("$.*", hasSize(8)))
                .andReturn();
    }

    @Test
    public void shouldRemoveFoundPost() throws Exception {
        this.mockMvc.perform(delete(FoundPostController.URI + "4")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Found announcement deleted. (ID: 4)")))
                .andReturn();
    }

    @Test
    public void shouldVerifyInvalidRemoveFoundPostArgument() throws Exception {
        this.mockMvc.perform(delete(FoundPostController.URI + "abc")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Your request has issued a malformed or illegal request")))
                .andReturn();
    }

    @Test
    public void shouldVerifyInvalidRemoveFoundPostId() throws Exception {
        this.mockMvc.perform(delete(FoundPostController.URI + "999")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Found pet announcement with ID: '999' not found")))
                .andReturn();
    }

    private Set<Colour> setColours() {
        Colour c1 = new Colour(PetColour.Brown);
        Colour c2 = new Colour(PetColour.Black);
        return new HashSet<>(Arrays.asList(c1, c2));
    }
}