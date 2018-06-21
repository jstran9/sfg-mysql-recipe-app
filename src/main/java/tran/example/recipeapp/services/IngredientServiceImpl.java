package tran.example.recipeapp.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tran.example.recipeapp.commands.IngredientCommand;
import tran.example.recipeapp.converters.IngredientToIngredientCommand;
import tran.example.recipeapp.domain.Recipe;
import tran.example.recipeapp.repositories.RecipeRepository;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private final RecipeRepository recipeRepository;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;

    @Autowired
    public IngredientServiceImpl(RecipeRepository recipeRepository, IngredientToIngredientCommand ingredientToIngredientCommand) {
        this.recipeRepository = recipeRepository;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if(!recipeOptional.isPresent()) {
            log.error("recipe with id: " + recipeId + " cannot be found");
        }

        Recipe recipe = recipeOptional.get();

        Optional<IngredientCommand> ingredientCommand = recipe.getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .map(ingredientToIngredientCommand::convert)
                .findFirst();

        if(!ingredientCommand.isPresent()) {
            log.error("could not find the ingredient with id: " + ingredientId + " inside of the recipe with id: " + recipeId);
        }

        return ingredientCommand.get();
    }
}
