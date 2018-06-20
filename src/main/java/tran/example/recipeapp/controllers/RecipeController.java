package tran.example.recipeapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import tran.example.recipeapp.services.RecipeService;

@Controller
@RequestMapping({RecipeController.RECIPE_BASE_URL})
public class RecipeController {

    public static final String RECIPE_BASE_URL = "/recipe";
    RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @RequestMapping("/show/{id}")
    public String getRecipeById(@PathVariable String id, Model model) {
        model.addAttribute("recipe", recipeService.findRecipeById(Long.valueOf(id)));

        return "recipe/show";
    }
}
