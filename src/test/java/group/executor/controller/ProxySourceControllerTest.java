package group.executor.controller;

import group.executor.controller.convertor.ConvertorObjectToString;
import group.executor.model.ProxyConfigHolder;
import group.executor.model.ProxyCredentials;
import group.executor.model.ProxyNetworkConfig;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class ProxySourceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void addProxy() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/proxy/add")
                        .content(ConvertorObjectToString
                                .asJsonString(new ProxyConfigHolder(new ProxyNetworkConfig("hostName", 8080),
                                        new ProxyCredentials("username", "password"))))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}