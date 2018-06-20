package tran.example.recipeapp.services;

import tran.example.recipeapp.domain.Recipe;

import java.util.Set;

public interface RecipeService {
    Set<Recipe> getRecipes();

    Recipe findRecipeById(Long id);
}
