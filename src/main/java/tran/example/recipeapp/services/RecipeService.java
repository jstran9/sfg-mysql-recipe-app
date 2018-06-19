package tran.example.recipeapp.services;

import tran.example.recipeapp.domain.Recipe;

import java.util.List;

public interface RecipeService {
    List<Recipe> getRecipes();
}
