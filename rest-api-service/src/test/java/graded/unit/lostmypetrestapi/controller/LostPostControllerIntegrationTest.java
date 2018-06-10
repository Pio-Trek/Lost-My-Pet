package graded.unit.lostmypetrestapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import graded.unit.lostmypetrestapi.LostMyPetRestApiApplication;
import graded.unit.lostmypetrestapi.entity.locations.Location;
import graded.unit.lostmypetrestapi.entity.pets.*;
import graded.unit.lostmypetrestapi.entity.posts.Lost;
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

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.AnyOf.anyOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*
 * INTEGRATION TEST FOR LOST PET ANNOUNCEMENT CONTROLLER LAYER
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = LostMyPetRestApiApplication.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.JVM)
public class LostPostControllerIntegrationTest {

    private MockMvc mockMvc;

    private User member = new User("test-id-user1", "password123", "user1@email.com", "Test1", "User1", new Location("West Vinewood"));

    private Dog dog = new Dog("Max", PetGender.Male, 2, false, true, new byte[]{10, 20, 30}, "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", setColours(), new DogBreed("Chihuahua"));
    private Lost announcement = new Lost(new Location("Oreton"), member, dog, new Date());

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        announcement.setEnabled(false);
    }

    @Test
    public void shouldFetchLostPosts() throws Exception {
        this.mockMvc.perform(get(LostPostController.URI)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].postType", equalTo("lost")))
                .andExpect(jsonPath("$[0].addedDate").exists())
                .andExpect(jsonPath("$[0].location.id").exists())
                .andExpect(jsonPath("$[0].location.name").exists())
                .andExpect(jsonPath("$[0].pet.type", anyOf(is("dog"), is("cat"))))
                .andExpect(jsonPath("$[0].lostDate").exists())
                .andExpect(jsonPath("$[0].enabled").exists())
                .andExpect(jsonPath("$[0].*", hasSize(8)))
                .andReturn();
    }

    @Test
    public void shouldFetchLostPostsByMemberId() throws Exception {
        this.mockMvc.perform(get(LostPostController.URI + "/member/test-id-user1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].postType", equalTo("lost")))
                .andExpect(jsonPath("$[0].addedDate").exists())
                .andExpect(jsonPath("$[0].location.id").exists())
                .andExpect(jsonPath("$[0].location.name").exists())
                .andExpect(jsonPath("$[0].pet.type", anyOf(is("dog"), is("cat"))))
                .andExpect(jsonPath("$[0].lostDate").exists())
                .andExpect(jsonPath("$[0].enabled").exists())
                .andExpect(jsonPath("$[0].*", hasSize(8)))
                .andReturn();
    }

    @Test
    public void shouldVerifyInvalidPostsByMemberId() throws Exception {
        this.mockMvc.perform(get(LostPostController.URI + "/member/fake-user")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("User with ID: 'fake-user' not found"))
                .andReturn();
    }

    @Test
    public void shouldFetchLostDogsPosts() throws Exception {
        this.mockMvc.perform(get(LostPostController.URI + "dogs")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].postType", equalTo("lost")))
                .andExpect(jsonPath("$[0].addedDate").exists())
                .andExpect(jsonPath("$[0].location.id").exists())
                .andExpect(jsonPath("$[0].location.name").exists())
                .andExpect(jsonPath("$[0].pet.type").value("dog"))
                .andExpect(jsonPath("$[0].lostDate").exists())
                .andExpect(jsonPath("$[0].enabled").exists())
                .andExpect(jsonPath("$[0].*", hasSize(8)))
                .andReturn();
    }

    @Test
    public void shouldFetchLostCatsPosts() throws Exception {
        this.mockMvc.perform(get(LostPostController.URI + "cats")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].postType", equalTo("lost")))
                .andExpect(jsonPath("$[0].addedDate").exists())
                .andExpect(jsonPath("$[0].location.id").exists())
                .andExpect(jsonPath("$[0].location.name").exists())
                .andExpect(jsonPath("$[0].pet.type").value("cat"))
                .andExpect(jsonPath("$[0].lostDate").exists())
                .andExpect(jsonPath("$[0].enabled").exists())
                .andExpect(jsonPath("$[0].*", hasSize(8)))
                .andReturn();
    }

    @Test
    public void shouldVerifyInvalidFetchPetArgument() throws Exception {
        this.mockMvc.perform(get(LostPostController.URI + "bears")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Your request has issued a malformed or illegal request")))
                .andReturn();
    }

    @Test
    public void shouldFetchLostPostById() throws Exception {
        this.mockMvc.perform(get(LostPostController.URI + "1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.postType", equalTo("lost")))
                .andExpect(jsonPath("$.addedDate").exists())
                .andExpect(jsonPath("$.location.id").exists())
                .andExpect(jsonPath("$.location.name").exists())
                .andExpect(jsonPath("$.pet.type", anyOf(is("dog"), is("cat"))))
                .andExpect(jsonPath("$.lostDate").exists())
                .andExpect(jsonPath("$.enabled").exists())
                .andExpect(jsonPath("$.*", hasSize(8)))
                .andReturn();
    }

    @Test
    public void shouldVerifyInvalidFetchLostPostById() throws Exception {
        this.mockMvc.perform(get(LostPostController.URI + "999")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Lost pet announcement with ID: '999' not found"))
                .andReturn();
    }

    @Test
    public void shouldVerifyInvalidFetchLostPostArgument() throws Exception {
        this.mockMvc.perform(get(LostPostController.URI + "abc")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Your request has issued a malformed or illegal request"))
                .andReturn();
    }

    @Test
    public void shouldInsertLostPost() throws Exception {
        //convert lost pet announcement object to JSON with Jackson
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(announcement);

        this.mockMvc.perform(post(LostPostController.URI)
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
        announcement.setLostDate(null);
        String json = mapper.writeValueAsString(announcement);

        this.mockMvc.perform(post(LostPostController.URI)
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
    public void shouldUpdateLostPost() throws Exception {
        //assign ID that already exist in database
        announcement.setId(1L);
        announcement.getPet().setId(1L);

        //update (change) random details
        announcement.getPet().setAge(5);
        announcement.setLostDate(new Date());

        //convert Admin object to JSON with Jackson
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(announcement);

        this.mockMvc.perform(put(LostPostController.URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.postType", equalTo("lost")))
                .andExpect(jsonPath("$.addedDate").exists())
                .andExpect(jsonPath("$.location.id").exists())
                .andExpect(jsonPath("$.location.name").exists())
                .andExpect(jsonPath("$.pet.type", anyOf(is("dog"), is("cat"))))
                .andExpect(jsonPath("$.pet.age").value(5))
                .andExpect(jsonPath("$.lostDate").exists())
                .andExpect(jsonPath("$.enabled").exists())
                .andExpect(jsonPath("$.*", hasSize(8)))
                .andReturn();
    }

    @Test
    public void shouldVerifyInvalidUpdateLostPostId() throws Exception {
        announcement.setId(999L);

        //convert Admin object to JSON with Jackson
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(announcement);

        this.mockMvc.perform(put(LostPostController.URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Lost pet announcement with ID: '999' not found")))
                .andReturn();
    }

    @Test
    public void shouldVerifyInvalidUpdateLostPost() throws Exception {
        announcement.setId(1L);

        //update (change) some details
        announcement.setLostDate(null);

        //convert Admin object to JSON with Jackson
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(announcement);

        this.mockMvc.perform(put(LostPostController.URI)
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
        announcement.setId(2L);
        announcement.getPet().setId(999L);

        //convert Admin object to JSON with Jackson
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(announcement);

        this.mockMvc.perform(put(LostPostController.URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Pet ID is not valid or found. Updating pet ID: 2. Existing pet ID: 999.")))
                .andReturn();
    }

    @Test
    public void shouldSetEnabledLostPost() throws Exception {

        //convert Admin object to JSON with Jackson
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(announcement);

        this.mockMvc.perform(get(LostPostController.URI + "2")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.postType", equalTo("lost")))
                .andExpect(jsonPath("$.addedDate").exists())
                .andExpect(jsonPath("$.location.id").exists())
                .andExpect(jsonPath("$.location.name").exists())
                .andExpect(jsonPath("$.pet.type", anyOf(is("dog"), is("cat"))))
                .andExpect(jsonPath("$.pet.age").value(3))
                .andExpect(jsonPath("$.lostDate").exists())
                .andExpect(jsonPath("$.enabled").value(true))
                .andExpect(jsonPath("$.*", hasSize(8)))
                .andReturn();
    }

    @Test
    public void shouldRemoveLostPost() throws Exception {
        this.mockMvc.perform(delete(LostPostController.URI + "1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Lost announcement deleted. (ID: 1)")))
                .andReturn();
    }

    @Test
    public void shouldVerifyInvalidRemoveLostPostArgument() throws Exception {
        this.mockMvc.perform(delete(LostPostController.URI + "abc")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Your request has issued a malformed or illegal request")))
                .andReturn();
    }

    @Test
    public void shouldVerifyInvalidRemoveLostPostId() throws Exception {
        this.mockMvc.perform(delete(LostPostController.URI + "999")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Lost pet announcement with ID: '999' not found")))
                .andReturn();
    }

    private Set<Colour> setColours() {
        Colour c1 = new Colour(PetColour.Golden);
        Colour c2 = new Colour(PetColour.Tan);
        Colour c3 = new Colour(PetColour.Lilac);
        Colour c4 = new Colour(PetColour.White);
        return new HashSet<>(Arrays.asList(c1, c2, c3, c4));
    }
}