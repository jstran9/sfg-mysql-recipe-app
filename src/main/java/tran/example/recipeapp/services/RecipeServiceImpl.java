package tran.example.recipeapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tran.example.recipeapp.domain.Recipe;
import tran.example.recipeapp.repositories.RecipeRepository;

import java.util.LinkedList;
import java.util.List;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public List<Recipe> getRecipes() {
        List<Recipe> recipes = new LinkedList<>();
        recipeRepository.findAll().forEach(recipes::add);
        return recipes;
    }
}
