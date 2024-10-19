package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Duration;
import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FilmorateApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private User user1;
	private User user2;
	private String userJson1;
	private String userJson2;
	private Film film1;
	private Film film2;
	private String filmJson1;
	private String filmJson2;

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

		film1 = Film.builder()
				.id(2L)
				.name("Движение вверх")
				.description("Фильм о баскетболе")
				.releaseDate(LocalDate.of(2015, 10, 12))
				.duration(Duration.ofSeconds(8500))
				.build();
		filmJson1 = objectMapper.writeValueAsString(film1);

		film2 = Film.builder()
				.id(11L)
				.name("Интерстеллар")
				.description("Фильм про космос")
				.releaseDate(LocalDate.of(2012, 3, 19))
				.duration(Duration.ofMinutes(9200))
				.build();
		filmJson2 = objectMapper.writeValueAsString(film2);
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
		assertThrows(ServletException.class, () -> mockMvc.perform(post("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(badJson1))
				.andExpect(status().isBadRequest()));

		user1.setEmail("johngmail.com");
		String badJson2 = objectMapper.writeValueAsString(user1);
		assertThrows(ServletException.class, () -> mockMvc.perform(post("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(badJson2))
				.andExpect(status().isBadRequest()));

		user1.setEmail("john@gmail.com");
		String badJson3 = objectMapper.writeValueAsString(user1);
		assertThrows(ServletException.class, () -> mockMvc.perform(post("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(badJson3))
				.andExpect(status().isBadRequest()));

		user1.setEmail("newjohn@gmail.com");
		user1.setLogin("");
		String badJson4 = objectMapper.writeValueAsString(user1);
		assertThrows(ServletException.class, () -> mockMvc.perform(post("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(badJson4))
				.andExpect(status().isBadRequest()));

		user1.setEmail("newjohn1@gmail.com");
		user1.setLogin("john 21");
		String badJson5 = objectMapper.writeValueAsString(user1);
		assertThrows(ServletException.class, () -> mockMvc.perform(post("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(badJson5))
				.andExpect(status().isBadRequest()));

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
		assertThrows(ServletException.class, () -> mockMvc.perform(post("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(badJson6))
				.andExpect(status().isBadRequest()));
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

		User user4 = User.builder()
				.id(1L)
				.name("NewJohn")
				.email("newjohn@gmail.com")
				.login("newjohn21")
				.birthday(LocalDate.of(2005, 9, 12))
				.build();

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
		assertThrows(ServletException.class, () -> mockMvc.perform(put("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(badJson1))
				.andExpect(status().isBadRequest()));

		user3.setEmail("newjohn@gmail.com");
		user3.setId(1L);
		String badJson2 = objectMapper.writeValueAsString(user3);
		assertThrows(ServletException.class, () -> mockMvc.perform(put("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(badJson2))
				.andExpect(status().isBadRequest()));

		user3.setEmail("johngmail.com");
		String badJson3 = objectMapper.writeValueAsString(user3);
		assertThrows(ServletException.class, () -> mockMvc.perform(put("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(badJson3))
				.andExpect(status().isBadRequest()));

		user3.setLogin("john 21");
		user3.setEmail("newjohn3@gmail.com");
		String badJson4 = objectMapper.writeValueAsString(user3);
		assertThrows(ServletException.class, () -> mockMvc.perform(put("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(badJson4))
				.andExpect(status().isBadRequest()));

		user3.setLogin("newjohn22");
		user3.setEmail("newjohn4@gmail.com");
		user3.setBirthday(LocalDate.of(2040, 9, 12));
		String badJson5 = objectMapper.writeValueAsString(user3);
		assertThrows(ServletException.class, () -> mockMvc.perform(put("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(badJson5))
				.andExpect(status().isBadRequest()));

		User user5 = User.builder()
				.id(1L)
				.name("")
				.email("")
				.login("")
				.birthday(null)
				.build();
		String userJson5 = objectMapper.writeValueAsString(user5);
		mockMvc.perform(put("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(userJson5))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.name").value(user4.getName()))
				.andExpect(jsonPath("$.email").value(user4.getEmail()))
				.andExpect(jsonPath("$.login").value(user4.getLogin()))
				.andExpect(jsonPath("$.birthday").value(user4.getBirthday().toString()));
	}

	@Test
	void postFilm() throws Exception {
		mockMvc.perform(post("/films")
						.contentType(MediaType.APPLICATION_JSON)
						.content(filmJson1))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.name").value(film1.getName()))
				.andExpect(jsonPath("$.description").value(film1.getDescription()))
				.andExpect(jsonPath("$.releaseDate").value(film1.getReleaseDate().toString()))
				.andExpect(jsonPath("$.duration").value(film1.getDuration().toString()));

		mockMvc.perform(post("/films")
						.contentType(MediaType.APPLICATION_JSON)
						.content(filmJson2))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(2))
				.andExpect(jsonPath("$.name").value(film2.getName()))
				.andExpect(jsonPath("$.description").value(film2.getDescription()))
				.andExpect(jsonPath("$.releaseDate").value(film2.getReleaseDate().toString()))
				.andExpect(jsonPath("$.duration").value(film2.getDuration().toString()));

		film1.setName("");
		String badJson1 = objectMapper.writeValueAsString(film1);
		assertThrows(ServletException.class, () -> mockMvc.perform(post("/films")
						.contentType(MediaType.APPLICATION_JSON)
						.content(badJson1))
				.andExpect(status().isBadRequest()));

		film1.setName("Движение вверх");
		film1.setDescription("Есть победы, которые меняют ход истории." +
				" Победы духа, победы страны, победы всего мира." +
				" Таким триумфом стали легендарные «три секунды» -" +
				" выигрыш сборной СССР по баскетболу на роковой мюнхенской Олимпиаде 1972 г. " +
				"Впервые за 36 лет была повержена «непобедимая» команда США." +
				" Никто даже помыслить не мог о том, что это возможно – " +
				"обыграть великолепных непогрешимых американцев на Олимпийских играх! " +
				"Никто, кроме советских баскетболистов (русских и грузин, украинцев и казахов, белорусов и литовцев)");
		String badJson2 = objectMapper.writeValueAsString(film1);
		assertThrows(ServletException.class, () -> mockMvc.perform(post("/films")
						.contentType(MediaType.APPLICATION_JSON)
						.content(badJson2))
				.andExpect(status().isBadRequest()));

		film1.setName("Фильм о баскетболе");
		film1.setReleaseDate(LocalDate.of(1700, 1, 1));
		String badJson3 = objectMapper.writeValueAsString(film1);
		assertThrows(ServletException.class, () -> mockMvc.perform(post("/films")
						.contentType(MediaType.APPLICATION_JSON)
						.content(badJson3))
				.andExpect(status().isBadRequest()));

		film1.setReleaseDate(LocalDate.of(1900, 1, 1));
		film1.setDuration(Duration.ofSeconds(-600));
		String badJson4 = objectMapper.writeValueAsString(film1);
		assertThrows(ServletException.class, () -> mockMvc.perform(post("/films")
						.contentType(MediaType.APPLICATION_JSON)
						.content(badJson4))
				.andExpect(status().isBadRequest()));
	}

	@Test
	void getFilms() throws Exception {
		mockMvc.perform(post("/films")
				.contentType(MediaType.APPLICATION_JSON)
				.content(filmJson1));

		mockMvc.perform(post("/films")
				.contentType(MediaType.APPLICATION_JSON)
				.content(filmJson2));

		mockMvc.perform(get("/films")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].name").value(film1.getName()))
				.andExpect(jsonPath("$[0].description").value(film1.getDescription()))
				.andExpect(jsonPath("$[0].releaseDate").value(film1.getReleaseDate().toString()))
				.andExpect(jsonPath("$[0].duration").value(film1.getDuration().toString()))
				.andExpect(jsonPath("$[1].id").value(2))
				.andExpect(jsonPath("$[1].name").value(film2.getName()))
				.andExpect(jsonPath("$[1].description").value(film2.getDescription()))
				.andExpect(jsonPath("$[1].releaseDate").value(film2.getReleaseDate().toString()))
				.andExpect(jsonPath("$[1].duration").value(film2.getDuration().toString()));
	}

	@Test
	void putFilm() throws Exception {
		mockMvc.perform(post("/films")
				.contentType(MediaType.APPLICATION_JSON)
				.content(filmJson1));

		Film film3 = Film.builder()
				.id(1L)
				.name("Новое движение вверх")
				.description("Новый фильм о баскетболе")
				.releaseDate(LocalDate.of(2024, 1, 2))
				.duration(Duration.ofSeconds(7200))
				.build();
		String filmJson3 = objectMapper.writeValueAsString(film3);

		Film film4 = Film.builder()
				.id(1L)
				.name("Новое движение вверх")
				.description("Новый фильм о баскетболе")
				.releaseDate(LocalDate.of(2024, 1, 2))
				.duration(Duration.ofSeconds(7200))
				.build();

		mockMvc.perform(put("/films")
						.contentType(MediaType.APPLICATION_JSON)
						.content(filmJson3))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.name").value(film3.getName()))
				.andExpect(jsonPath("$.description").value(film3.getDescription()))
				.andExpect(jsonPath("$.releaseDate").value(film3.getReleaseDate().toString()))
				.andExpect(jsonPath("$.duration").value(film3.getDuration().toString()));

		film3.setId(7L);
		String badJson1 = objectMapper.writeValueAsString(film3);
		assertThrows(ServletException.class, () -> mockMvc.perform(put("/films")
						.contentType(MediaType.APPLICATION_JSON)
						.content(badJson1))
				.andExpect(status().isBadRequest()));

		film3.setId(1L);
		film3.setDescription("Есть победы, которые меняют ход истории." +
				" Победы духа, победы страны, победы всего мира." +
				" Таким триумфом стали легендарные «три секунды» -" +
				" выигрыш сборной СССР по баскетболу на роковой мюнхенской Олимпиаде 1972 г. " +
				"Впервые за 36 лет была повержена «непобедимая» команда США." +
				" Никто даже помыслить не мог о том, что это возможно – " +
				"обыграть великолепных непогрешимых американцев на Олимпийских играх! " +
				"Никто, кроме советских баскетболистов (русских и грузин, украинцев и казахов, белорусов и литовцев)");
		String badJson2 = objectMapper.writeValueAsString(film3);
		assertThrows(ServletException.class, () -> mockMvc.perform(put("/films")
						.contentType(MediaType.APPLICATION_JSON)
						.content(badJson2))
				.andExpect(status().isBadRequest()));

		film3.setDescription("Фильм");
		film3.setReleaseDate(LocalDate.of(1700, 1, 2));
		String badJson3 = objectMapper.writeValueAsString(film3);
		assertThrows(ServletException.class, () -> mockMvc.perform(put("/films")
						.contentType(MediaType.APPLICATION_JSON)
						.content(badJson3))
				.andExpect(status().isBadRequest()));

		film3.setReleaseDate(LocalDate.of(1900, 1, 2));
		film3.setDuration(Duration.ofSeconds(-60));
		String badJson4 = objectMapper.writeValueAsString(film3);
		assertThrows(ServletException.class, () -> mockMvc.perform(put("/films")
						.contentType(MediaType.APPLICATION_JSON)
						.content(badJson4))
				.andExpect(status().isBadRequest()));

		Film film5 = Film.builder()
				.id(1L)
				.name("")
				.description("")
				.releaseDate(null)
				.duration(null)
				.build();
		String filmJson5 = objectMapper.writeValueAsString(film5);

		mockMvc.perform(put("/films")
						.contentType(MediaType.APPLICATION_JSON)
						.content(filmJson5))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.name").value(film4.getName()))
				.andExpect(jsonPath("$.description").value(film4.getDescription()))
				.andExpect(jsonPath("$.releaseDate").value(film4.getReleaseDate().toString()))
				.andExpect(jsonPath("$.duration").value(film4.getDuration().toString()));
	}
}
