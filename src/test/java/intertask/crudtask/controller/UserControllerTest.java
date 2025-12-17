package intertask.crudtask.controller;


import intertask.crudtask.model.User;
import intertask.crudtask.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User user1;
    private User user2;

    @Test
    void getAllUsers_shouldReturnListOfUsersWithSuccessResponse() throws Exception {
        // give
        user1 = createUser(1L, "Alice", "alice@example.com", 30, 70000.0);
        user2 = createUser(2L, "Bob", "bob@example.com", 25, 60000.0);
        List<User> users = Arrays.asList(user1, user2);

        when(userService.getAllUsers()).thenReturn(users);

        // when then
        mockMvc.perform(get("/api/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.count").value(2))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.message").value("List of all users retrieved successfully"));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getUsersSorted_withDefaultParams_shouldUseIdAsc() throws Exception {
        // give
        user1 = createUser(1L, "Alice", "alice@example.com", 30, 70000.0);
        List<User> users = List.of(user1);
        when(userService.getUsersSortedByField("id", "asc")).thenReturn(users);

        // when then
        mockMvc.perform(get("/api/users/sorted")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.sort.field").value("id"))
                .andExpect(jsonPath("$.sort.direction").value("asc"));

        verify(userService).getUsersSortedByField("id", "asc");
    }

    @Test
    void getUsersSorted_withValidParams_shouldReturnSortedData() throws Exception {
        // give
        user1 = createUser(1L, "Alice", "alice@example.com", 30, 70000.0);
        when(userService.getUsersSortedByField("name", "desc")).thenReturn(List.of(user1));

        // when then
        mockMvc.perform(get("/api/users/sorted")
                        .param("field", "name")
                        .param("direction", "desc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sort.field").value("name"))
                .andExpect(jsonPath("$.sort.direction").value("desc"));

        verify(userService).getUsersSortedByField("name", "desc");
    }

    @Test
    void getUsersSorted_withInvalidField_shouldReturnBadRequestWithDetails() throws Exception {
        // given
        String invalidField = "invalid";
        String message = "Invalid sort field: " + invalidField;
        when(userService.getUsersSortedByField(invalidField, "asc"))
                .thenThrow(new IllegalArgumentException(message));

        // when then
        mockMvc.perform(get("/api/users/sorted")
                        .param("field", invalidField)
                        .param("direction", "asc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value(message))
                .andExpect(jsonPath("$.availableFields", containsInAnyOrder(
                        "id", "name", "email", "age", "salary", "createdAt"
                )))

                .andExpect(jsonPath("$.availableDirections", containsInAnyOrder("asc", "desc")));
    }


// вспомогательный метод
    private User createUser(Long id, String name, String email, int age, double salary) {
        User u = new User();
        u.setId(id);
        u.setName(name);
        u.setEmail(email);
        u.setAge(age);
        u.setSalary(salary);
        u.setCreatedAt(LocalDateTime.now());
        return u;
    }
}
