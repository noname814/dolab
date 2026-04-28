package com.example.demo;

import com.example.demo.controller.MainController;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepo;
import com.example.demo.repository.BookRepo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MainController.class)
public class LoginTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepo userRepo; // mock repo

    @MockBean   // 🔥 ADD THIS
    private BookRepo bookRepo;

    @Test
    void testLoginSuccess() throws Exception {

        User user = new User();
        user.setUsername("admin");
        user.setPassword("123");

        when(userRepo.findByUsernameAndPassword("admin", "123"))
                .thenReturn(user);

        mockMvc.perform(post("/login")
                .param("username", "admin")
                .param("password", "123"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void testLoginFailure() throws Exception {

        when(userRepo.findByUsernameAndPassword("admin", "wrong"))
                .thenReturn(null);

        mockMvc.perform(post("/login")
                .param("username", "admin")
                .param("password", "wrong"))
                .andExpect(status().isOk());
    }
}