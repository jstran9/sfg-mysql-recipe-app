package tran.example.recipeapp.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tran.example.recipeapp.domain.Recipe;
import tran.example.recipeapp.repositories.RecipeRepository;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class RecipeServiceImplTest {

    RecipeServiceImpl recipeService;

    @Mock
    RecipeRepository recipeRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        recipeService = new RecipeServiceImpl(recipeRepository);
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
}