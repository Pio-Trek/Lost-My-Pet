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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 * INTEGRATION TEST FOR REPORT CONTROLLER LAYER
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = LostMyPetRestApiApplication.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.JVM)
public class ReportControllerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void shouldFetchReports() throws Exception {
        this.mockMvc.perform(get(ReportController.URI)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].sendDate").exists())
                .andExpect(jsonPath("$[0].message").exists())
                .andExpect(jsonPath("$[0].user").exists())
                .andExpect(jsonPath("$[0].user").isNotEmpty())
                .andExpect(jsonPath("$[0].announcement").exists())
                .andExpect(jsonPath("$[0].announcement").isNotEmpty())
                .andExpect(jsonPath("$[0].*", hasSize(5)))
                .andReturn();
    }

    @Test
    public void shouldRemoveReport() throws Exception {
        this.mockMvc.perform(delete(ReportController.URI + "1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Report deleted. (ID: 1)")))
                .andReturn();
    }

    @Test
    public void shouldVerifyInvalidRemoveReport() throws Exception {
        this.mockMvc.perform(delete(ReportController.URI + "99A")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Your request has issued a malformed or illegal request")))
                .andReturn();
    }
}