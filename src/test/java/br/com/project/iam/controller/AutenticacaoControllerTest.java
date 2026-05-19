package br.com.project.iam.controller;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AutenticacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Devolver código HTTP 400 quando as informações de login forem inválidas")
    void loginCenario1() throws Exception {
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Devolver código HTTP 401 ou 403 quando a senha estiver errada")
    void loginCenario2() throws Exception {
        String jsonLoginFalso = """
                {
                    "email": "invalido@email.com",
                    "senha": "123"
                }    
                """;

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonLoginFalso))
                .andExpect(status().isUnauthorized());
    }


}
