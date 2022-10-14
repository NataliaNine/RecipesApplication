package recipes.RecipesApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    public Optional<User> findById(String username) {
        return userRepository.findById(username);
    }

    public User save(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
