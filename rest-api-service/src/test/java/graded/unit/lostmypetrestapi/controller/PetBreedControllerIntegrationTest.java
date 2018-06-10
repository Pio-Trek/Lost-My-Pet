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

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 * INTEGRATION TEST FOR PET BREED CONTROLLER LAYER
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = LostMyPetRestApiApplication.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.JVM)
public class PetBreedControllerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void shoudFetchAllDogBreeds() throws Exception {
        this.mockMvc.perform(get(PetBreedController.URI + "/dogs")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("Other"))
                .andExpect(jsonPath("$[0].*", hasSize(2)))
                .andExpect(jsonPath("$.*", hasSize(31)))
                .andReturn();
    }

    @Test
    public void shouldFetchAllCatBreeds() throws Exception {
        this.mockMvc.perform(get(PetBreedController.URI + "/cats")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("Other"))
                .andExpect(jsonPath("$[0].*", hasSize(2)))
                .andExpect(jsonPath("$.*", hasSize(16)))
                .andReturn();
    }

    @Test
    public void shouldVerifyInvalidPedBreedParameter() throws Exception {
        this.mockMvc.perform(get(PetBreedController.URI + "/birds")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }
}