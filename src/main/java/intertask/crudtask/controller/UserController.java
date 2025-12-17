package intertask.crudtask.controller;

import intertask.crudtask.model.User;
import intertask.crudtask.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // получаем просто всех
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        List<User> users = userService.getAllUsers();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("count", users.size());
        response.put("data", users);
        response.put("message", "List of all users retrieved successfully");

        return ResponseEntity.ok(response);
    }

    // получаем с сортировкой
    @GetMapping("/sorted")
    public ResponseEntity<?> getUsersSorted(
            @RequestParam(value = "field", defaultValue = "id") String field,
            @RequestParam(value = "direction", defaultValue = "asc") String direction) {

        try {
            List<User> users = userService.getUsersSortedByField(field, direction);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("count", users.size());
            response.put("data", users);
            response.put("sort", Map.of(
                    "field", field,
                    "direction", direction
            ));
            response.put("message", "Users sorted by " + field + " in " + direction + " order");

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            errorResponse.put("availableFields", List.of("id", "name", "email", "age", "salary", "createdAt"));
            errorResponse.put("availableDirections", List.of("asc", "desc"));

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}

