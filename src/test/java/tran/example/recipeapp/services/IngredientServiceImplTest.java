package tran.example.recipeapp.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tran.example.recipeapp.commands.IngredientCommand;
import tran.example.recipeapp.converters.IngredientToIngredientCommand;
import tran.example.recipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import tran.example.recipeapp.domain.Ingredient;
import tran.example.recipeapp.domain.Recipe;
import tran.example.recipeapp.repositories.RecipeRepository;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class IngredientServiceImplTest {

    @Mock
    private RecipeRepository recipeRepository;

    private IngredientToIngredientCommand ingredientToIngredientCommand;

    IngredientService ingredientService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        ingredientToIngredientCommand = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
        ingredientService = new IngredientServiceImpl(recipeRepository, ingredientToIngredientCommand);
    }

    @Test
    public void findByRecipeIdAndIngredientId() throws Exception {
        // given
        Recipe recipe = new Recipe();
        recipe.setId(1L);

        Ingredient ingredient = new Ingredient();
        ingredient.setId(1L);

        Ingredient ingredientTwo = new Ingredient();
        ingredientTwo.setId(2L);

        Ingredient ingredientThree = new Ingredient();
        ingredientThree.setId(3L);

        recipe.addIngredient(ingredient);
        recipe.addIngredient(ingredientTwo);
        recipe.addIngredient(ingredientThree);

        Optional<Recipe> recipeOptional = Optional.of(recipe);
        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);

        // when
        IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndIngredientId(1L, 1L);

        // then
        assertEquals(Long.valueOf(1L), ingredientCommand.getRecipeId());
        assertEquals(Long.valueOf(1L), ingredientCommand.getId());
        verify(recipeRepository, times(1)).findById(anyLong());
    }
}