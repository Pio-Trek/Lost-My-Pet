package graded.unit.lostmypetrestapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import graded.unit.lostmypetrestapi.LostMyPetRestApiApplication;
import graded.unit.lostmypetrestapi.entity.locations.Location;
import graded.unit.lostmypetrestapi.entity.users.Role;
import graded.unit.lostmypetrestapi.entity.users.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*
 * INTEGRATION TEST FOR MEMBER CONTROLLER LAYER
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = LostMyPetRestApiApplication.class)
@SpringBootTest
public class UserControllerIntegrationTest {

    private MockMvc mockMvc;

    //create Member object for tests
    private User member = new User(null, "member-member@gmail.com", "secret", "John", "Brown", new Location("Burton"));

    //create Admin object for tests
    private User admin = new User(null, "admin-admin@gmail.com", "secret", "TomTheAdmin", null, null);

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void shouldFetchUsers() throws Exception {
        this.mockMvc.perform(get(UserController.URI)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].email").exists())
                .andExpect(jsonPath("$[0].password").exists())
                .andExpect(jsonPath("$[0].firstName").exists())
                .andExpect(jsonPath("$[0].lastName").exists())
                .andExpect(jsonPath("$[0].enabled").exists())
                .andExpect(jsonPath("$[0].location.id").exists())
                .andExpect(jsonPath("$[0].location.name").exists())
                .andExpect(jsonPath("$[0].roles").exists())
                .andExpect(jsonPath("$[0].*", hasSize(9)))
                .andReturn();
    }

    @Test
    public void shouldFetchUserByEmail() throws Exception {
        this.mockMvc.perform(get(UserController.URI + "?email=user2@email.com")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value("user2@email.com"))
                .andExpect(jsonPath("$.password").exists())
                .andExpect(jsonPath("$.firstName").exists())
                .andExpect(jsonPath("$.lastName").exists())
                .andExpect(jsonPath("$.roles[0].id").value(Role.ROLE_USER.getId()))
                .andExpect(jsonPath("$.roles[0].role").value(Role.ROLE_USER.name()))
                .andExpect(jsonPath("$.roles.*", hasSize(1)))
                .andExpect(jsonPath("$.location.id").exists())
                .andExpect(jsonPath("$.location.name").exists())
                .andExpect(jsonPath("$.*", hasSize(9)))
                .andReturn();
    }

    @Test
    public void shouldVerifyInvalidUserEmail() throws Exception {
        this.mockMvc.perform(get(UserController.URI + "?email=fake@email.com")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("User with email address: 'fake@email.com' not found"))
                .andReturn();
    }

    @Test
    public void shouldRespondTrueWhenUserExistsByEmail() throws Exception {
        this.mockMvc.perform(get(UserController.URI + "/exists/user1@email.com")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("true")))
                .andReturn();
    }

    @Test
    public void shouldRespondFalseWhenUserNotExistsByEmail() throws Exception {
        this.mockMvc.perform(get(UserController.URI + "/exists/user1@email.com")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("false")))
                .andReturn();
    }

    @Test
    public void shouldFetchUserByConfirmationToken() throws Exception {
        this.mockMvc.perform(get(UserController.URI + "?token=0123token0123")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value("user2@email.com"))
                .andExpect(jsonPath("$.password").exists())
                .andExpect(jsonPath("$.firstName").exists())
                .andExpect(jsonPath("$.lastName").exists())
                .andExpect(jsonPath("$.roles[0].id").value(Role.ROLE_USER.getId()))
                .andExpect(jsonPath("$.roles[0].role").value(Role.ROLE_USER.name()))
                .andExpect(jsonPath("$.roles.*", hasSize(1)))
                .andExpect(jsonPath("$.location.id").exists())
                .andExpect(jsonPath("$.location.name").exists())
                .andExpect(jsonPath("$.*", hasSize(9)))
                .andReturn();
    }

    @Test
    public void shouldRespondNullWhenInvalidConfirmationToken() throws Exception {
        this.mockMvc.perform(get(UserController.URI + "?token=random-token")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("null")))
                .andReturn();
    }

    @Test
    public void shouldInsertMember() throws Exception {
        //convert Member object to JSON with Jackson
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(member);

        mockMvc.perform(post(UserController.URI + "/member")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void shouldVerifyInvalidInsertMember() throws Exception {
        //convert Member object to JSON with Jackson
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        member.setEmail(null);
        String json = ow.writeValueAsString(member);

        mockMvc.perform(post(UserController.URI + "/member")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Your request has issued a malformed or illegal request"))
                .andReturn();
    }

    @Test
    public void shouldVerifyEmailIsNotUnique() throws Exception {

        //set email that already exist in database
        member.setEmail("user1@email.com");

        //convert Member object to JSON with Jackson
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(member);

        mockMvc.perform(post(UserController.URI + "/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("This email address is already reserved, specify a different one"))
                .andReturn();
    }

    @Test
    public void shouldInsertAdmin() throws Exception {
        //convert Admin object to JSON with Jackson
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(admin);

        mockMvc.perform(post(UserController.URI + "/admin")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void shouldVerifyInvalidInsertAdmin() throws Exception {
        //convert Member object to JSON with Jackson
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        admin.setEmail("abc");
        String json = ow.writeValueAsString(admin);

        mockMvc.perform(post(UserController.URI + "/admin")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Your request has issued a malformed or illegal request"))
                .andReturn();
    }

    @Test
    public void shouldUpdateMember() throws Exception {
        //assign ID that already exist in database
        member.setId("test-id-user1");

        //update (change) random details
        member.setEmail("new-member@email.com");
        member.setLastName("Taylor");

        //convert Member object to JSON with Jackson
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(member);

        this.mockMvc.perform(put(UserController.URI + "/member")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("test-id-user1"))
                .andExpect(jsonPath("$.email").value("new-member@email.com"))
                .andExpect(jsonPath("$.password").value("secret"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Taylor"))
                .andExpect(jsonPath("$.enabled").value("true"))
                .andExpect(jsonPath("$.location.id").value(3))
                .andExpect(jsonPath("$.location.name").value(member.getLocation().getName()))
                .andExpect(jsonPath("$.roles[0].id").value(Role.ROLE_USER.getId()))
                .andExpect(jsonPath("$.roles[0].role").value(Role.ROLE_USER.name()))
                .andExpect(jsonPath("$.roles.*", hasSize(1)))
                .andExpect(jsonPath("$.*", hasSize(9)))
                .andReturn();
    }

    @Test
    public void shouldVerifyInvalidUpdateMemberId() throws Exception {
        //convert Member object to JSON with Jackson
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        member.setId("999");
        String json = ow.writeValueAsString(member);

        System.out.println(json);

        this.mockMvc.perform(put(UserController.URI + "/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("User with ID: '999' not found"))
                .andReturn();
    }

    @Test
    public void shouldVerifyInvalidPropertyNameWhenUpdateMember() throws Exception {
        this.mockMvc.perform(put(UserController.URI + "/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{  \"id\" : \"test-id-user1\",  \"em\" : \"member-member@gmail.com\",  \"password\" : \"secret\",  \"firstName\" : \"John\",  \"lastName\" : \"Brown\",  \"enabled\" : null,  \"c\" : null,  \"location\" : {    \"id\" : null,    \"name\" : \"Burton\"  },  \"roles\" : null }")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Your request has issued a malformed or illegal request"))
                .andReturn();
    }

    @Test
    public void shouldUpdateAdmin() throws Exception {
        //assign ID that already exist in database
        admin.setId("test-id-admin");

        //update (change) random details
        admin.setEmail("new-admin@email.com");
        admin.setFirstName("Mary");

        //convert Member object to JSON with Jackson
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(admin);

        this.mockMvc.perform(put(UserController.URI + "/admin")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("test-id-admin"))
                .andExpect(jsonPath("$.email").value("new-admin@email.com"))
                .andExpect(jsonPath("$.password").value("secret"))
                .andExpect(jsonPath("$.firstName").value("Mary"))
                .andExpect(jsonPath("$.lastName").isEmpty())
                .andExpect(jsonPath("$.enabled").isEmpty())
                .andExpect(jsonPath("$.confirmationToken").isEmpty())
                .andExpect(jsonPath("$.location").isEmpty())
                .andExpect(jsonPath("$.roles[0].id").value(Role.ROLE_ADMIN.getId()))
                .andExpect(jsonPath("$.roles[0].role").value(Role.ROLE_ADMIN.name()))
                .andExpect(jsonPath("$.roles.*", hasSize(1)))
                .andExpect(jsonPath("$.*", hasSize(9)))
                .andReturn();
    }

    @Test
    public void shouldVerifyInvalidUpdateAdminId() throws Exception {
        //convert Member object to JSON with Jackson
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        admin.setId("abc");
        String json = ow.writeValueAsString(admin);

        System.out.println(json);

        this.mockMvc.perform(put(UserController.URI + "/admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("User with ID: 'abc' not found"))
                .andReturn();
    }

    @Test
    public void shouldVerifyInvalidPropertyNameWhenUpdateAdmin() throws Exception {
        this.mockMvc.perform(put(UserController.URI + "/admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{  \"igd\" : \"test-id-admin\",  \"egmail\" : \"admin-admin@gmail.com\",  \"password\" : \"secret\",  \"firstName\" : \"TomTheAdmin\",  \"lastName\" : null,  \"enabled\" : null,  \"confirmationToken\" : null,  \"location\" : null,  \"roles\" : null }")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Your request has issued a malformed or illegal request"))
                .andReturn();
    }

    @Test
    public void shouldSetMemberTokenToResetPassword() throws Exception {
        this.mockMvc.perform(patch(UserController.URI + "/member?reset=user1@email.com")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("test-id-user1"))
                .andExpect(jsonPath("$.email").value("user1@email.com"))
                .andExpect(jsonPath("$.password").exists())
                .andExpect(jsonPath("$.firstName").value("Test1"))
                .andExpect(jsonPath("$.lastName").value("User1"))
                .andExpect(jsonPath("$.enabled").value("true"))
                .andExpect(jsonPath("$.confirmationToken").exists())
                .andExpect(jsonPath("$.confirmationToken").isNotEmpty())
                .andExpect(jsonPath("$.location.id").value(1))
                .andExpect(jsonPath("$.location.name").value("West Vinewood"))
                .andExpect(jsonPath("$.roles[0].id").value(Role.ROLE_USER.getId()))
                .andExpect(jsonPath("$.roles[0].role").value(Role.ROLE_USER.name()))
                .andExpect(jsonPath("$.roles.*", hasSize(1)))
                .andExpect(jsonPath("$.*", hasSize(9)))
                .andReturn();
    }

    @Test
    public void shouldVerifyInvalidMemberTokenToResetPassword() throws Exception {
        this.mockMvc.perform(patch(UserController.URI + "/member?reset=noemail@email.com")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User with email address: 'noemail@email.com' not found"))
                .andReturn();
    }

    @Test
    public void shouldRemoveMember() throws Exception {
        this.mockMvc.perform(delete(UserController.URI + "test-id-user1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User with ID: 'test-id-user1' deleted"))
                .andReturn();
    }

    @Test
    public void shouldVerifyInvalidMemberRemove() throws Exception {
        this.mockMvc.perform(delete(UserController.URI + "a1a1a1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User with ID: 'a1a1a1' not found"))
                .andReturn();
    }


    @Test
    public void shouldThrowExceptionWhenLocationNotExist() throws Exception {

        //assign ID that already exist in database
        member.setId("test-id-user1");

        //set fake location to the object
        member.setLocation(new Location("Fake Location"));

        //convert Member object to JSON with Jackson
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(member);

        mockMvc.perform(put(UserController.URI + "/member")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Location name: 'Fake Location' not found"))
                .andReturn();
    }

}