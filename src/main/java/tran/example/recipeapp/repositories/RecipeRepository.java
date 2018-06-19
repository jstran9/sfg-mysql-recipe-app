package tran.example.recipeapp.repositories;

import org.springframework.data.repository.CrudRepository;
import tran.example.recipeapp.domain.Recipe;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {
}
