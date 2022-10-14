package recipes.RecipesApplication;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {
    @Override
    Optional<Recipe> findById(Long aLong);

    @Override
    Recipe save(Recipe recipe);

    @Override
    void deleteById(Long id);

    List<Recipe> findAllByCategory(String category);

    List<Recipe> findAllByName(String name);
}
