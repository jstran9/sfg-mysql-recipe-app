package tran.example.recipeapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import tran.example.recipeapp.domain.Category;
import tran.example.recipeapp.domain.UnitOfMeasure;
import tran.example.recipeapp.repositories.CategoryRepository;
import tran.example.recipeapp.repositories.UnitOfMeasureRepository;
import tran.example.recipeapp.services.RecipeService;

import java.util.Optional;

@Controller
public class IndexController {

//    private CategoryRepository categoryRepository;
//
//    @Autowired
//    public IndexController(CategoryRepository categoryRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
//        this.categoryRepository = categoryRepository;
//        this.unitOfMeasureRepository = unitOfMeasureRepository;
//    }

    private RecipeService recipeService;

    @Autowired
    public IndexController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @RequestMapping({"", "/", "index"})
    public String getIndexPage(Model model) {

//        Optional<Category> categoryOptional = categoryRepository.findByDescription("American");
//        Optional<UnitOfMeasure> unitOfMeasureOptional = unitOfMeasureRepository.findByDescription("Teaspoon");
//        System.out.println("Category id is " + categoryOptional.get().getId());
//        System.out.println("UnitOfMeasure id is " + unitOfMeasureOptional.get().getId());

        model.addAttribute("recipes", recipeService.getRecipes());
        return "index";
    }
}
