package tran.example.recipeapp.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tran.example.recipeapp.domain.Recipe;
import tran.example.recipeapp.repositories.RecipeRepository;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        List<Recipe> recipeData = new LinkedList<>();
        recipeData.add(recipe);

        // when
        when(recipeService.getRecipes()).thenReturn(recipeData);

        List<Recipe> recipeDataTwo = recipeService.getRecipes();

        // then
        assertEquals(recipeData.size(), recipeDataTwo.size());
        // the recipeRepository should be called once when you call the getRecipes.
        verify(recipeRepository, times(1)).findAll();
    }
}