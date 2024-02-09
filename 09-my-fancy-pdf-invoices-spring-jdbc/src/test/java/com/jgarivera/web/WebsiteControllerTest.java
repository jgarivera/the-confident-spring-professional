package com.jgarivera.web;

import com.jgarivera.context.ApplicationConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class WebsiteControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void it_shows_home_page() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello")));
    }

    @Test
    void it_shows_home_page_with_username() throws Exception {
        mockMvc.perform(get("/")
                        .queryParam("username", "bobby"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello")))
                .andExpect(content().string(containsString("bobby")));
    }

    @Test
    void it_shows_home_page_with_unusual_username() throws Exception {
        mockMvc.perform(get("/")
                        .queryParam("username", "zenith"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello")))
                .andExpect(content().string(containsString("zenith")))
                .andExpect(content().string(containsString("unusual")));
    }

    @Test
    void it_shows_login_page() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Login")))
                .andExpect(content().string(containsString("Username")))
                .andExpect(content().string(containsString("Password")));
    }

    @Test
    void it_authenticates_on_login_page() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "johnny")
                        .param("password", "johnny"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/"));
    }

    @Test
    void it_fails_on_login_page() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "johnny")
                        .param("password", "michael"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("doesn't look like you")));
    }

    @Test
    void it_validates_login_page_username_field() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "")
                        .param("password", "michael"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("loginForm", "username"));

        mockMvc.perform(post("/login")
                        .param("username", "j")
                        .param("password", "michael"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("loginForm", "username"));

        mockMvc.perform(post("/login")
                        .param("username", "lewislewislewislewisl")
                        .param("password", "michael"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("loginForm", "username"));
    }

    @Test
    void it_validates_login_page_password_field() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "johnny")
                        .param("password", ""))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("loginForm", "password"));

        mockMvc.perform(post("/login")
                        .param("username", "johnny")
                        .param("password", "j"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("loginForm", "password"));

        mockMvc.perform(post("/login")
                        .param("username", "johnny")
                        .param("password", "lewislewislewislewisl"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("loginForm", "password"));
    }
}
