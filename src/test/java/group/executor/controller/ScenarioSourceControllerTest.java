package group.executor.controller;

import group.executor.controller.convertor.ConvertorObjectToJSON;
import group.executor.model.*;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class ScenarioSourceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void addProxy() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/scenario/add")
                        .content(ConvertorObjectToJSON
                                .asJsonString(getScenario()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private Scenario getScenario() {
        List<Step> steps = new ArrayList<>();
        steps.add(new Step("clickCss", "body > ul > li:nth-child(1) > a"));
        return new Scenario("test scenario 1", "http://info.cern.ch", steps);
    }
}