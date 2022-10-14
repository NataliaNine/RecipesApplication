package recipes.RecipesApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SpringBootApplication
@RestController
public class RecipesApplication {
    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private UserService userService;
    @Autowired
    private RecipeService recipeService;

    @Autowired
    private WebSecurityConfigurerImpl webSecurityConfigurer;
    private final Logger logger = LoggerFactory.getLogger(RecipesApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(RecipesApplication.class, args);
    }

    @GetMapping("/api/recipe/{id}")
    public ResponseEntity<?> getRecipe(@PathVariable long id) {
        Optional<Recipe> recipe = recipeService.findById(id);

        if (recipe.isPresent()) {
            return new ResponseEntity<>(recipe.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/api/recipe/search")
    public ResponseEntity<?> searchRecipe(@RequestParam Map<String, String> inputs) {
        String category = inputs.get("category");
        String name = inputs.get("name");

        if (category != null ^ name != null) {
            if (category != null) {
                List<Recipe> foundRecipes = recipeService.findAllByCategory(category);
                return new ResponseEntity<>(foundRecipes, HttpStatus.OK);
            } else {
                List<Recipe> foundRecipes = recipeService.findAllByName(name);
                return new ResponseEntity<>(foundRecipes, HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/api/register")
    public ResponseEntity<?> register(@RequestBody @Valid NewUser user) {
        logger.info("Called POST /api/register with: " + user);
        Optional<User> existingUser = userService.findById(user.getEmail());
        if (existingUser.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            userService.save(new User(user.getEmail(), user.getPassword()));
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @PostMapping("/api/recipe/new")
    public ResponseEntity<?> addRecipe(@RequestBody @Valid Recipe recipe, @AuthenticationPrincipal UserDetails user) {
        logger.info("Called POST /api/recipe/new with: " + recipe);
        LocalDateTime now = LocalDateTime.now();
        recipe.setDate(now.toString());
        recipe.setCreatedBy(user.getUsername());
        Recipe savedRecipe = recipeService.save(recipe);
        Map<String, Long> returnVal = new HashMap<>();
        returnVal.put("id", savedRecipe.getId());
        return new ResponseEntity<>(returnVal, HttpStatus.OK);
    }

    @PutMapping("/api/recipe/{id}")
    public ResponseEntity<?> updateRecipe(@PathVariable long id, @RequestBody @Valid Recipe updatedRecipe, @AuthenticationPrincipal UserDetails user) {
        Optional<Recipe> existingRecipe = recipeService.findById(id);
        if (existingRecipe.isPresent()) {
            String recipeOwner = existingRecipe.get().getCreatedBy();
            String updater = user.getUsername();
            if (recipeOwner.equals(updater)) {
                updatedRecipe.setId(id);
                updatedRecipe.setDate(LocalDateTime.now().toString());
                updatedRecipe.setCreatedBy(updater);
                recipeService.save(updatedRecipe);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/api/recipe/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable long id, @AuthenticationPrincipal UserDetails user) {
        Optional<Recipe> recipe = recipeService.findById(id);
        if (recipe.isPresent()) {
            String recipeOwner = recipe.get().getCreatedBy();
            String updater = user.getUsername();
            if (recipeOwner.equals(updater)) {
                recipeService.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }




}
