package intertask.crudtask.service;


import intertask.crudtask.model.User;
import intertask.crudtask.repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getUsersSortedByField(String sortField, String sortDirection) {
        validateSortField(sortField);

        // Определение направления сортировки
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDirection)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        // Создаем объект сортировки
        Sort sort = Sort.by(direction, sortField);

        return userRepository.findAll(sort);
    }


    private void validateSortField(String field) {
        List<String> validFields = Arrays.asList("id", "name", "email", "age", "salary", "createdAt");

        if (!validFields.contains(field)) {
            throw new IllegalArgumentException(
                    "Invalid sort field: " + field + ". Valid fields are: " +
                            String.join(", ", validFields)
            );
        }
    }


}