package intertask.crudtask.model;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private Integer age;

    @Column(name = "salary", columnDefinition = "NUMERIC(10,2)")
    private Double salary;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public User() {
    }

    public User(String name, String email, Integer age, Double salary) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.salary = salary;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }


    // Метод для автоматической установки времени
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }


}