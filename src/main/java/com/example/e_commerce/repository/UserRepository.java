package com.example.e_commerce.repository;
import com.example.e_commerce.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
}
