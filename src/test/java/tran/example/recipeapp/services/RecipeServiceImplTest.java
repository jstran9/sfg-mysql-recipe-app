package tran.example.recipeapp.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tran.example.recipeapp.commands.RecipeCommand;
import tran.example.recipeapp.converters.RecipeCommandToRecipe;
import tran.example.recipeapp.converters.RecipeToRecipeCommand;
import tran.example.recipeapp.domain.Recipe;
import tran.example.recipeapp.exceptions.NotFoundException;
import tran.example.recipeapp.repositories.RecipeRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class RecipeServiceImplTest {

    RecipeServiceImpl recipeService;

    @Mock
    RecipeRepository recipeRepository;

    @Mock
    RecipeToRecipeCommand recipeToRecipeCommand;

    @Mock
    RecipeCommandToRecipe recipeCommandToRecipe;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        recipeService = new RecipeServiceImpl(recipeRepository, recipeCommandToRecipe, recipeToRecipeCommand);
    }

    @Test
    public void getRecipes() throws Exception {

        Recipe recipe = new Recipe();

        // given
        Set<Recipe> recipeData = new HashSet<>();
        recipeData.add(recipe);

        // when
        when(recipeService.getRecipes()).thenReturn(recipeData);

        Set<Recipe> recipeDataTwo = recipeService.getRecipes();

        // then
        assertEquals(recipeData.size(), recipeDataTwo.size());
        // the recipeRepository should be called once when you call the getRecipes.
        verify(recipeRepository, times(1)).findAll();
    }

    @Test
    public void getRecipeById() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);

        Recipe returnedRecipe = recipeService.findRecipeById(1L);

        assertNotNull("Null recipe returned", returnedRecipe);
        verify(recipeRepository, times(1)).findById(anyLong());
        verify(recipeRepository, never()).findAll();
    }

    @Test
    public void findCommandById() throws Exception {
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(1L);

        when(recipeToRecipeCommand.convert(any())).thenReturn(recipeCommand);

        assertNotNull("Null recipe command returned", recipeCommand);
        verify(recipeRepository, times(0)).findById(anyLong());
        verify(recipeRepository, never()).findAll();
    }

    @Test
    public void deleteById() throws Exception {
        // given
        Long idToDelete = 2L;
        // when
        recipeService.deleteById(idToDelete);
        // then
        verify(recipeRepository, times(1)).deleteById(anyLong());
    }

    @Test(expected = NotFoundException.class)
    public void getRecipeByIdNotFound() throws Exception {
        Optional<Recipe> recipeOptional = Optional.empty();

        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);

        Recipe recipeReturned = recipeService.findRecipeById(1L);
    }
}