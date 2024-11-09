package com.example.e_commerce.serviceimpl;
import com.example.e_commerce.entity.User;
import com.example.e_commerce.repository.UserRepository;
import com.example.e_commerce.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User signUp(User user) {
        // Encrypt the password before saving it
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // Sign In
    @Override
    public Optional<User> signIn(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            System.out.println("presnet");
            User user = userOptional.get();
            // Check if the password matches the stored hash
            if (passwordEncoder.matches(password, user.getPassword())) {
                // Password matches, return the user

                return Optional.of(user);
            } else {
                // Password doesn't match
                return Optional.empty();
            }
        }
        // User with given email doesn't exist
        return Optional.empty();
    }
}
