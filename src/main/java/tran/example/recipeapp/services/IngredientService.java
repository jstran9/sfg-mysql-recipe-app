package tran.example.recipeapp.services;

import tran.example.recipeapp.commands.IngredientCommand;


public interface IngredientService {
    IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId);
}
