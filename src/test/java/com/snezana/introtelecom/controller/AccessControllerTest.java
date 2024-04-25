package com.snezana.introtelecom.controller;

import com.snezana.introtelecom.dto.UserLoginDTO;
import com.snezana.introtelecom.security.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(AccessController.class)
@WithMockUser(username ="sneza1", password = "snezana")
class AccessControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;


    @Test
    void testLogin() throws Exception {
        String username = "sneza1";
        String password = "snezana";
        UserLoginDTO userDTO = new UserLoginDTO(username, password);
        String access_token = "$2a$10$8uf1WrqnVzxRpavBtngqdebOB7wTxGdhCwIZoY/GFvXPHD58StAnW";
        Map<String, String> token = new HashMap<>();
        token.put("access_token", access_token);

        when(authenticationService.login(userDTO)).thenReturn(token);

        mockMvc.perform(post("/access/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("username", "sneza1")
                        .param("password", "snezana")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.data.access_token").value(access_token))
                .andExpect(jsonPath("$.responseDate").isNotEmpty())
                .andExpect(jsonPath("$.success").value(true));
    }
}