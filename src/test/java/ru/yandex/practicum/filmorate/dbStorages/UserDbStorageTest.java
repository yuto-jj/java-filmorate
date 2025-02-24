package ru.yandex.practicum.filmorate.dbStorages;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dal.FriendshipDbStorage;
import ru.yandex.practicum.filmorate.storage.dal.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.dal.mappers.FriendshipRowMapper;
import ru.yandex.practicum.filmorate.storage.dal.mappers.UserRowMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class, UserRowMapper.class, FriendshipDbStorage.class, FriendshipRowMapper.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserDbStorageTest {
    private final UserDbStorage userStorage;
    private final FriendshipDbStorage friendshipStorage;
    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setUp() {
        user1 = User.builder()
                .id(2L)
                .name("John")
                .email("john@gmail.com")
                .login("john21")
                .birthday(LocalDate.of(2000, 2, 17))
                .build();

        user2 = User.builder()
                .id(5L)
                .name("Mike")
                .email("mike099@mail.ru")
                .login("mike33")
                .birthday(LocalDate.of(1985, 5, 22))
                .build();

        user3 = User.builder()
                .name("Tom")
                .login("tommy33")
                .email("tom33@gmail.com")
                .birthday(LocalDate.of(1950, 9, 9))
                .build();
    }

    @Test
    @Order(1)
    public void addAndGetUser() {
        userStorage.addUser(user1);
        User testUser = userStorage.getUser(1L);
        assertThat(testUser.getId()).isEqualTo(1L);
        assertThat(testUser.getName()).isEqualTo(user1.getName());
        assertThat(testUser.getEmail()).isEqualTo(user1.getEmail());
        assertThat(testUser.getLogin()).isEqualTo(user1.getLogin());
        assertThat(testUser.getBirthday()).isEqualTo(user1.getBirthday());

        userStorage.addUser(user2);
        User testUser2 = userStorage.getUser(2L);
        assertThat(testUser2.getId()).isEqualTo(2L);
        assertThat(testUser2.getName()).isEqualTo(user2.getName());
        assertThat(testUser2.getEmail()).isEqualTo(user2.getEmail());
        assertThat(testUser2.getLogin()).isEqualTo(user2.getLogin());
        assertThat(testUser2.getBirthday()).isEqualTo(user2.getBirthday());
    }

    @Test
    @Order(2)
    public void updateUser() {
        userStorage.addUser(user1);
        user1.setId(3L);
        user1.setName("update");
        user1.setEmail("update@mail.ru");
        user1.setLogin("updated_login");
        user1.setBirthday(LocalDate.of(2010, 1, 31));
        userStorage.updateUser(user1);
        User updatedUser = userStorage.getUser(3L);
        assertThat(updatedUser.getId()).isEqualTo(3L);
        assertThat(updatedUser.getName()).isEqualTo(user1.getName());
        assertThat(updatedUser.getEmail()).isEqualTo(user1.getEmail());
        assertThat(updatedUser.getLogin()).isEqualTo(user1.getLogin());
        assertThat(updatedUser.getBirthday()).isEqualTo(user1.getBirthday());
    }

    @Test
    @Order(3)
    public void testFindUserById() {
        userStorage.addUser(user1);
        userStorage.addUser(user2);
        User receivedUser = userStorage.getUser(4L);
        assertThat(receivedUser.getId()).isEqualTo(4L);
        assertThat(receivedUser.getName()).isEqualTo(user1.getName());
        assertThat(receivedUser.getEmail()).isEqualTo(user1.getEmail());
        assertThat(receivedUser.getLogin()).isEqualTo(user1.getLogin());
        assertThat(receivedUser.getBirthday()).isEqualTo(user1.getBirthday());
        User receivedUser2 = userStorage.getUser(5L);
        assertThat(receivedUser2.getId()).isEqualTo(5L);
        assertThat(receivedUser2.getName()).isEqualTo(user2.getName());
        assertThat(receivedUser2.getEmail()).isEqualTo(user2.getEmail());
        assertThat(receivedUser2.getLogin()).isEqualTo(user2.getLogin());
        assertThat(receivedUser2.getBirthday()).isEqualTo(user2.getBirthday());
    }

    @Test
    @Order(4)
    public void testFindUsers() {
        userStorage.addUser(user1);
        userStorage.addUser(user2);
        userStorage.addUser(user3);
        List<User> users = userStorage.getUsers();
        assertThat(users).hasSize(3);
        assertThat(users).usingRecursiveFieldByFieldElementComparator().contains(user1);
        assertThat(users).usingRecursiveFieldByFieldElementComparator().contains(user2);
        assertThat(users).usingRecursiveFieldByFieldElementComparator().contains(user3);
    }

    @Test
    @Order(5)
    public void testAddAndRemoveFriend() {
        userStorage.addUser(user1);
        userStorage.addUser(user2);
        friendshipStorage.updateFriendship(user1.getId(), user2.getId());
        Set<User> friends1 = friendshipStorage.getFriends(user1.getId());
        assertThat(friends1).hasSize(1);
        assertThat(friends1).usingRecursiveFieldByFieldElementComparator().contains(user2);
        Set<User> friends2 = friendshipStorage.getFriends(user2.getId());
        assertThat(friends2).hasSize(0);
        friendshipStorage.updateFriendship(user2.getId(), user1.getId());
        Set<User> friends3 = friendshipStorage.getFriends(user2.getId());
        assertThat(friends3).hasSize(1);
        assertThat(friends3).usingRecursiveFieldByFieldElementComparator().contains(user1);

        userStorage.addUser(user3);
        friendshipStorage.updateFriendship(user2.getId(), user3.getId());
        friendshipStorage.updateFriendship(user3.getId(), user2.getId());
        Set<User> friends4 = friendshipStorage.getFriends(user3.getId());
        assertThat(friends4).hasSize(1);
        Set<User> friends5 = friendshipStorage.getFriends(user2.getId());
        assertThat(friends5).hasSize(2);

        friendshipStorage.deleteFriendship(user2.getId(), user3.getId());
        Set<User> friends6 = friendshipStorage.getFriends(user3.getId());
        assertThat(friends6).hasSize(1);
        Set<User> friends7 = friendshipStorage.getFriends(user2.getId());
        assertThat(friends7).hasSize(1);
        assertThat(friends7).usingRecursiveFieldByFieldElementComparator().contains(user1);
    }
}
