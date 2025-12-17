package intertask.crudtask.service;

import intertask.crudtask.model.User;
import intertask.crudtask.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setId(1L);
        user1.setName("Alice");
        user1.setEmail("alice@example.com");
        user1.setAge(30);
        user1.setSalary(70000.0);
        user1.setCreatedAt(LocalDateTime.now());

        user2 = new User();
        user2.setId(2L);
        user2.setName("Bob");
        user2.setEmail("bob@example.com");
        user2.setAge(25);
        user2.setSalary(60000.0);
        user2.setCreatedAt(LocalDateTime.now().minusDays(1));
    }

    @Test
    void getAllUsers_shouldReturnAllUsers() {
        // give
        List<User> users = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(users);

        // when
        List<User> result = userService.getAllUsers();

        // then
        assertThat(result).hasSize(2).containsExactlyInAnyOrder(user1, user2);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUsersSortedByField_withValidFieldAndAscDirection_shouldSortAsc() {
        // give
        List<User> users = Arrays.asList(user2, user1);
        when(userRepository.findAll(any(Sort.class))).thenReturn(users);

        // when
        List<User> result = userService.getUsersSortedByField("id", "asc");

        // then
        assertThat(result).isEqualTo(users);
        verify(userRepository).findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Test
    void getUsersSortedByField_withValidFieldAndDescDirection_shouldSortDesc() {
        // give
        List<User> users = Arrays.asList(user1, user2);
        when(userRepository.findAll(any(Sort.class))).thenReturn(users);

        // when
        List<User> result = userService.getUsersSortedByField("name", "DESC");

        // then
        assertThat(result).isEqualTo(users);
        verify(userRepository).findAll(Sort.by(Sort.Direction.DESC, "name"));
    }

    @Test
    void getUsersSortedByField_withInvalidField_shouldThrowIllegalArgumentException() {
        assertThatThrownBy(() -> userService.getUsersSortedByField("invalidField", "asc"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid sort field: invalidField")
                .hasMessageContaining("id, name, email, age, salary, createdAt");
    }

    @Test
    void getUsersSortedByField_caseInsensitiveDirection_shouldWork() {
        // give
        List<User> users = List.of(user1);
        when(userRepository.findAll(any(Sort.class))).thenReturn(users);

        // when
        List<User> result1 = userService.getUsersSortedByField("email", "ASC");
        List<User> result2 = userService.getUsersSortedByField("email", "desc");

        // then
        assertThat(result1).isNotNull();
        assertThat(result2).isNotNull();
        verify(userRepository, times(2)).findAll(any(Sort.class));

    }

    @Test
    void getUsersSortedByField_defaultToAsc_whenDirectionIsUnrecognized() {
        // give
        List<User> users = List.of(user1);
        when(userRepository.findAll(any(Sort.class))).thenReturn(users);

        // when
        userService.getUsersSortedByField("age", "up"); // unrecognized → defaults to ASC

        // then
        verify(userRepository).findAll(Sort.by(Sort.Direction.ASC, "age"));
    }
}