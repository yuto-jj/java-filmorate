package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = FilmorateApplication.class)
class FilmorateApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void postUser() throws JsonProcessingException {
		User user = User.builder()
				.id(2L)
				.name("John")
				.email("john@gmail.com")
				.login("john21")
				.birthday(LocalDate.of(2000, 2, 17))
				.build();

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		String userJson = objectMapper.writeValueAsString(user);

		String url = "http://localhost:" + port + "/users/";

		ResponseEntity<String> response = restTemplate.postForEntity(url, userJson, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		User user1 = objectMapper.readValue(response.getBody(), User.class);

		assertEquals(user.getId(), user1.getId());
		assertEquals(user.getName(), user1.getName());
		assertEquals(user.getEmail(), user1.getEmail());
		assertEquals(user.getLogin(), user1.getLogin());
		assertEquals(user.getBirthday(), user1.getBirthday());
	}
}
