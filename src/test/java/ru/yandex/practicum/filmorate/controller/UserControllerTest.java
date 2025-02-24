package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import ru.yandex.practicum.filmorate.FilmorateApplicationTests;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
public class UserControllerTest extends FilmorateApplicationTests {
    private User user1;
    private User user2;
    private String userJson1;
    private String userJson2;

    @BeforeEach
    void beforeEach() throws JsonProcessingException {
        user1 = User.builder()
                .id(2L)
                .name("John")
                .email("john@gmail.com")
                .login("john21")
                .birthday(LocalDate.of(2000, 2, 17))
                .build();
        userJson1 = objectMapper.writeValueAsString(user1);

        user2 = User.builder()
                .id(5L)
                .name("Mike")
                .email("mike099@mail.ru")
                .login("mike33")
                .birthday(LocalDate.of(1985, 5, 22))
                .build();
        userJson2 = objectMapper.writeValueAsString(user2);
    }

    @Test
    void postUsers() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(user1.getName()))
                .andExpect(jsonPath("$.email").value(user1.getEmail()))
                .andExpect(jsonPath("$.login").value(user1.getLogin()))
                .andExpect(jsonPath("$.birthday").value(user1.getBirthday().toString()));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value(user2.getName()))
                .andExpect(jsonPath("$.email").value(user2.getEmail()))
                .andExpect(jsonPath("$.login").value(user2.getLogin()))
                .andExpect(jsonPath("$.birthday").value(user2.getBirthday().toString()));

        user1.setEmail("");
        String badJson1 = objectMapper.writeValueAsString(user1);
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badJson1))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(status().isBadRequest());

        user1.setEmail("johngmail.com");
        String badJson2 = objectMapper.writeValueAsString(user1);
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badJson2))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(status().isBadRequest());

        user1.setEmail("john@gmail.com");
        String badJson3 = objectMapper.writeValueAsString(user1);
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badJson3))
                 .andExpect(jsonPath("$.error").exists())
                .andExpect(status().isBadRequest());

        user1.setEmail("newjohn@gmail.com");
        user1.setLogin("");
        String badJson4 = objectMapper.writeValueAsString(user1);
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badJson4))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(status().isBadRequest());

        user1.setEmail("newjohn1@gmail.com");
        user1.setLogin("john 21");
        String badJson5 = objectMapper.writeValueAsString(user1);
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badJson5))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(status().isBadRequest());

        user1.setLogin("newjohn21");
        user1.setEmail("newjohn2@gmail.com");
        user1.setName("");
        String nameJson = objectMapper.writeValueAsString(user1);
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nameJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(user1.getLogin()));

        user1.setEmail("newjohn3@gmail.com");
        user1.setBirthday((LocalDate.of(2040, 1, 1)));
        String badJson6 = objectMapper.writeValueAsString(user1);
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badJson6))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(status().isBadRequest());
    }

    @Test
    void getUsers() throws Exception {
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson1));

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson2));

        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value(user1.getName()))
                .andExpect(jsonPath("$[0].email").value(user1.getEmail()))
                .andExpect(jsonPath("$[0].login").value(user1.getLogin()))
                .andExpect(jsonPath("$[0].birthday").value(user1.getBirthday().toString()))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value(user2.getName()))
                .andExpect(jsonPath("$[1].email").value(user2.getEmail()))
                .andExpect(jsonPath("$[1].login").value(user2.getLogin()))
                .andExpect(jsonPath("$[1].birthday").value(user2.getBirthday().toString()));
    }

    @Test
    void putUser() throws Exception {
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson1));

        User user3 = User.builder()
                .id(1L)
                .name("NewJohn")
                .email("newjohn@gmail.com")
                .login("newjohn21")
                .birthday(LocalDate.of(2005, 9, 12))
                .build();
        String userJson3 = objectMapper.writeValueAsString(user3);

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson3))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(user3.getName()))
                .andExpect(jsonPath("$.email").value(user3.getEmail()))
                .andExpect(jsonPath("$.login").value(user3.getLogin()))
                .andExpect(jsonPath("$.birthday").value(user3.getBirthday().toString()));

        user3.setId(3L);
        String badJson1 = objectMapper.writeValueAsString(user3);
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badJson1))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(status().isNotFound());

        user3.setId(1L);
        user3.setEmail("johngmail.com");
        String badJson3 = objectMapper.writeValueAsString(user3);
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badJson3))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(status().isBadRequest());

        user3.setLogin("john 21");
        user3.setEmail("newjohn3@gmail.com");
        String badJson4 = objectMapper.writeValueAsString(user3);
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badJson4))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(status().isBadRequest());

        user3.setLogin("newjohn22");
        user3.setEmail("newjohn4@gmail.com");
        user3.setBirthday(LocalDate.of(2040, 9, 12));
        String badJson5 = objectMapper.writeValueAsString(user3);
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badJson5))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(status().isBadRequest());
    }
}
