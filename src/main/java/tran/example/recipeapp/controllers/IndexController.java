package tran.example.recipeapp.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import tran.example.recipeapp.services.RecipeService;

@Slf4j
@Controller
public class IndexController {

//    private CategoryRepository categoryRepository;
//
//    @Autowired
//    public IndexController(CategoryRepository categoryRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
//        this.categoryRepository = categoryRepository;
//        this.unitOfMeasureRepository = unitOfMeasureRepository;
//    }

    private final String INDEX_DEBUG_STATEMENT = "loading the index page!";
    private RecipeService recipeService;

    @Autowired
    public IndexController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @RequestMapping({"", "/", "index"})
    public String getIndexPage(Model model) {

        log.debug(INDEX_DEBUG_STATEMENT);
//        Optional<Category> categoryOptional = categoryRepository.findByDescription("American");
//        Optional<UnitOfMeasure> unitOfMeasureOptional = unitOfMeasureRepository.findByDescription("Teaspoon");
//        System.out.println("Category id is " + categoryOptional.get().getId());
//        System.out.println("UnitOfMeasure id is " + unitOfMeasureOptional.get().getId());

        model.addAttribute("recipes", recipeService.getRecipes());
        return "index";
    }
}
