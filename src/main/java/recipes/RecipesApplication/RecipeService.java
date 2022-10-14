package recipes.RecipesApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Configuration
public class RecipeService {
    @Autowired
    private RecipeRepository recipeRepository;


    public Optional<Recipe> findById(Long id) {
        return recipeRepository.findById(id);
    }

    public Recipe save(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public void deleteById(Long id) {
        recipeRepository.deleteById(id);
    }

    public List<Recipe> findAllByCategory(String category) {
        Iterable<Recipe> allRecipes = recipeRepository.findAll();
        List<Recipe> categoryRecipes = StreamSupport.stream(allRecipes.spliterator(), false)
                .filter(r -> category.equalsIgnoreCase(r.getCategory()))
                .sorted(Comparator.comparing(Recipe::getDate).reversed())
                .collect(Collectors.toList());

        return categoryRecipes;
    }

    public List<Recipe> findAllByName(String name) {
        Iterable<Recipe> allRecipes = recipeRepository.findAll();
        List<Recipe> nameRecipes = StreamSupport.stream(allRecipes.spliterator(), false)
                .filter(r -> r.getName().toLowerCase().contains(name.toLowerCase()))
                .sorted(Comparator.comparing(Recipe::getDate).reversed())
                .collect(Collectors.toList());

        return nameRecipes;
    }
}
