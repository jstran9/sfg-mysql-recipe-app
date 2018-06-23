package tran.example.recipeapp.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tran.example.recipeapp.domain.*;
import tran.example.recipeapp.repositories.CategoryRepository;
import tran.example.recipeapp.repositories.RecipeRepository;
import tran.example.recipeapp.repositories.UnitOfMeasureRepository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@Profile({"dev", "prod"})
public class BootStrapMySQL implements ApplicationListener<ContextRefreshedEvent> {

    private final static String SAMPLE_GUACAMOLE_RECIPE_NAME = "Perfect Guacamole";
    private final static String SAMPLE_TACO_RECIPE_NAME = "Spicy Grilled Chicken Taco";
    private static final String TEASPOON_UOM = "Teaspoon";
    private static final String TABLESPOON_UOM = "Tablespoon";
    private static final String CUP_UOM = "Cup";
    private static final String PINCH_UOM = "Pinch";
    private static final String OUNCE_UOM = "Ounce";
    private static final String EACH_UOM = "Each";
    private static final String PINT_UOM = "Pint";
    private static final String DASH_UOM = "Dash";
    private static final String UOM_CANT_BE_FOUND = " uom can't be found.";
    private static final String AMERICAN_CATEGORY = "American";
    private static final String ITALIAN_CATEGORY = "Italian";
    private static final String MEXICAN_CATEGORY = "Mexican";
    private static final String FAST_FOOD_CATEGORY = "Fast Food";
    private static final String CATEGORY_CANT_BE_FOUND = " category can't be found";

    private final CategoryRepository categoryRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final RecipeRepository recipeRepository;


    @Autowired
    public BootStrapMySQL(CategoryRepository categoryRepository, UnitOfMeasureRepository unitOfMeasureRepository,
                          RecipeRepository recipeRepository) {
        this.categoryRepository = categoryRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.recipeRepository = recipeRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        if (categoryRepository.count() == 0L){
            log.debug("Loading Categories");
            saveCategories();
        }

        if (unitOfMeasureRepository.count() == 0L){
            log.debug("Loading UOMs");
            saveUnitOfMeasures();
        }

        Map<String, Category> categories = getCategories();
        Map<String, UnitOfMeasure> unitOfMeasures = getUnitOfMeasures();

        saveSampleRecipes(categories, unitOfMeasures);
    }

    private void saveSampleRecipes(Map<String, Category> categories, Map<String, UnitOfMeasure> unitOfMeasures) {
        log.debug("testing to see if sample recipes need to be created");

        UnitOfMeasure eachUom = unitOfMeasures.get(EACH_UOM);
        UnitOfMeasure teaspoonUom = unitOfMeasures.get(TEASPOON_UOM);
        UnitOfMeasure tableSpoonUom = unitOfMeasures.get(TABLESPOON_UOM);
        UnitOfMeasure dashUom = unitOfMeasures.get(DASH_UOM);
        UnitOfMeasure cupsUom = unitOfMeasures.get(CUP_UOM);
        UnitOfMeasure pintUom = unitOfMeasures.get(PINT_UOM);
        Category americanCategory = categories.get(AMERICAN_CATEGORY);
        Category mexicanCategory = categories.get(MEXICAN_CATEGORY);
        if(!doesRecipeExist(SAMPLE_GUACAMOLE_RECIPE_NAME, getGuacRecipeDirections())) {
            createGuacRecipe(eachUom, teaspoonUom, tableSpoonUom, dashUom, americanCategory, mexicanCategory);
        }
        if(!doesRecipeExist(SAMPLE_TACO_RECIPE_NAME, getTacoRecipeDirections())) {
            createTacosRecipe(tableSpoonUom, teaspoonUom, eachUom, cupsUom, pintUom, americanCategory, mexicanCategory);
        }
    }


    private boolean doesRecipeExist(String recipeName, String recipeDirections) {
        /**
         * helper method that looks for a sample recipe based on the description name) and the directions.
         * this is a private method that is only used within this class, although it's possible that there will be
         * recipes with the exact same directions and name for simplicity I'm going to assume this case doesn't occur.
         */
        Optional<Recipe> recipeOptional = recipeRepository.findByDescriptionAndDirections(recipeName, recipeDirections);
        if(!recipeOptional.isPresent()) {
            return false;
        }
        return true;
    }

    private Map<String, Category> getCategories() {
        Map<String, Category> categories = new HashMap<>();

        Optional<Category> americanCategoryOptional = categoryRepository.findByDescription(AMERICAN_CATEGORY);
        if(!americanCategoryOptional.isPresent()) {
            throw new RuntimeException(AMERICAN_CATEGORY + CATEGORY_CANT_BE_FOUND);
        }
        Category cat1 = americanCategoryOptional.get();
        categories.put(AMERICAN_CATEGORY, cat1);

        Optional<Category> italianCategoryOptional = categoryRepository.findByDescription(ITALIAN_CATEGORY);
        if(!italianCategoryOptional.isPresent()) {
            throw new RuntimeException(ITALIAN_CATEGORY + CATEGORY_CANT_BE_FOUND);
        }
        Category cat2 = italianCategoryOptional.get();
        categories.put(ITALIAN_CATEGORY, cat2);

        Optional<Category> mexicanCategoryOptional = categoryRepository.findByDescription(MEXICAN_CATEGORY);
        if(!mexicanCategoryOptional.isPresent()) {
            throw new RuntimeException(MEXICAN_CATEGORY + CATEGORY_CANT_BE_FOUND);
        }
        Category cat3 = mexicanCategoryOptional.get();
        categories.put(MEXICAN_CATEGORY, cat3);

        Optional<Category> fastFoodCategoryOptional = categoryRepository.findByDescription(FAST_FOOD_CATEGORY);
        if(!fastFoodCategoryOptional.isPresent()) {
            throw new RuntimeException(FAST_FOOD_CATEGORY + CATEGORY_CANT_BE_FOUND);
        }
        Category cat4 = fastFoodCategoryOptional.get();
        categories.put(FAST_FOOD_CATEGORY, cat4);

        return categories;
    }

    private void saveCategories(){
        Category cat1 = new Category();
        cat1.setDescription(AMERICAN_CATEGORY);
        categoryRepository.save(cat1);

        Category cat2 = new Category();
        cat2.setDescription(ITALIAN_CATEGORY);
        categoryRepository.save(cat2);

        Category cat3 = new Category();
        cat3.setDescription(MEXICAN_CATEGORY);
        categoryRepository.save(cat3);

        Category cat4 = new Category();
        cat4.setDescription(FAST_FOOD_CATEGORY);
        categoryRepository.save(cat4);
    }

    private Map<String, UnitOfMeasure> getUnitOfMeasures(){
        Map<String, UnitOfMeasure> unitOfMeasures = new HashMap<>();

        Optional<UnitOfMeasure> teaspoonUomOptional = unitOfMeasureRepository.findByDescription(TEASPOON_UOM);
        if(!teaspoonUomOptional.isPresent()) {
            throw new RuntimeException(TEASPOON_UOM + UOM_CANT_BE_FOUND);
        }
        UnitOfMeasure teaspoonUom = teaspoonUomOptional.get();
        unitOfMeasures.put(TEASPOON_UOM, teaspoonUom);

        Optional<UnitOfMeasure> tablespoonUomOptional = unitOfMeasureRepository.findByDescription(TABLESPOON_UOM);
        if(!tablespoonUomOptional.isPresent()) {
            throw new RuntimeException(TABLESPOON_UOM + UOM_CANT_BE_FOUND);
        }
        UnitOfMeasure tablespoonUom = tablespoonUomOptional.get();
        unitOfMeasures.put(TABLESPOON_UOM, tablespoonUom);

        Optional<UnitOfMeasure> cupUomOptional = unitOfMeasureRepository.findByDescription(CUP_UOM);
        if(!cupUomOptional.isPresent()) {
            throw new RuntimeException(CUP_UOM + UOM_CANT_BE_FOUND);
        }
        UnitOfMeasure cupUom = cupUomOptional.get();
        unitOfMeasures.put(CUP_UOM, cupUom);

        Optional<UnitOfMeasure> pinchUomOptional = unitOfMeasureRepository.findByDescription(PINCH_UOM);
        if(!pinchUomOptional.isPresent()) {
            throw new RuntimeException(PINCH_UOM + UOM_CANT_BE_FOUND);
        }
        UnitOfMeasure pinchUom = pinchUomOptional.get();
        unitOfMeasures.put(PINCH_UOM, pinchUom);

        Optional<UnitOfMeasure> ounceUomOptional = unitOfMeasureRepository.findByDescription(OUNCE_UOM);
        if(!ounceUomOptional.isPresent()) {
            throw new RuntimeException(OUNCE_UOM + UOM_CANT_BE_FOUND);
        }
        UnitOfMeasure ounceUom = ounceUomOptional.get();
        unitOfMeasures.put(OUNCE_UOM, ounceUom);

        Optional<UnitOfMeasure> eachUomOptional = unitOfMeasureRepository.findByDescription(EACH_UOM);
        if(!eachUomOptional.isPresent()) {
            throw new RuntimeException(EACH_UOM + UOM_CANT_BE_FOUND);
        }
        UnitOfMeasure eachUom = eachUomOptional.get();
        unitOfMeasures.put(EACH_UOM, eachUom);

        Optional<UnitOfMeasure> pintUomOptional = unitOfMeasureRepository.findByDescription(PINT_UOM);
        if(!pintUomOptional.isPresent()) {
            throw new RuntimeException(PINT_UOM + UOM_CANT_BE_FOUND);
        }
        UnitOfMeasure pintUom = pintUomOptional.get();
        unitOfMeasures.put(PINT_UOM, pintUom);

        Optional<UnitOfMeasure> dashUomOptional = unitOfMeasureRepository.findByDescription(DASH_UOM);
        if(!dashUomOptional.isPresent()) {
            throw new RuntimeException(DASH_UOM + UOM_CANT_BE_FOUND);
        }
        UnitOfMeasure dashUom = dashUomOptional.get();
        unitOfMeasures.put(DASH_UOM, dashUom);

        return unitOfMeasures;
    }

    private void saveUnitOfMeasures() {
        UnitOfMeasure uom1 = new UnitOfMeasure();
        uom1.setDescription(TEASPOON_UOM);
        unitOfMeasureRepository.save(uom1);

        UnitOfMeasure uom2 = new UnitOfMeasure();
        uom2.setDescription(TABLESPOON_UOM);
        unitOfMeasureRepository.save(uom2);

        UnitOfMeasure uom3 = new UnitOfMeasure();
        uom3.setDescription(CUP_UOM);
        unitOfMeasureRepository.save(uom3);

        UnitOfMeasure uom4 = new UnitOfMeasure();
        uom4.setDescription(PINCH_UOM);
        unitOfMeasureRepository.save(uom4);

        UnitOfMeasure uom5 = new UnitOfMeasure();
        uom5.setDescription(OUNCE_UOM);
        unitOfMeasureRepository.save(uom5);

        UnitOfMeasure uom6 = new UnitOfMeasure();
        uom6.setDescription(EACH_UOM);
        unitOfMeasureRepository.save(uom6);

        UnitOfMeasure uom7 = new UnitOfMeasure();
        uom7.setDescription(PINT_UOM);
        unitOfMeasureRepository.save(uom7);

        UnitOfMeasure uom8 = new UnitOfMeasure();
        uom8.setDescription(DASH_UOM);
        unitOfMeasureRepository.save(uom8);
    }

    private void createGuacRecipe(UnitOfMeasure eachUom, UnitOfMeasure teapoonUom, UnitOfMeasure tableSpoonUom,
                                    UnitOfMeasure dashUom, Category americanCategory, Category mexicanCategory) {
        //Yummy Guac
        Recipe guacRecipe = new Recipe();
        guacRecipe.setDescription(SAMPLE_GUACAMOLE_RECIPE_NAME);
        guacRecipe.setPrepTime(10);
        guacRecipe.setCookTime(0);
        guacRecipe.setDifficulty(Difficulty.EASY);
        guacRecipe.setDirections(getGuacRecipeDirections());

        Notes guacNotes = new Notes();
        guacNotes.setRecipeNotes("For a very quick guacamole just take a 1/4 cup of salsa and mix it in with your mashed avocados.\n" +
                "Feel free to experiment! One classic Mexican guacamole has pomegranate seeds and chunks of peaches in it (a Diana Kennedy favorite). Try guacamole with added pineapple, mango, or strawberries.\n" +
                "The simplest version of guacamole is just mashed avocados with salt. Don't let the lack of availability of other ingredients stop you from making guacamole.\n" +
                "To extend a limited supply of avocados, add either sour cream or cottage cheese to your guacamole dip. Purists may be horrified, but so what? It tastes great.\n" +
                "\n" +
                "\n" +
                "Read more: http://www.simplyrecipes.com/recipes/perfect_guacamole/#ixzz4jvoun5ws");

        guacRecipe.setNotes(guacNotes);

        addIngredientsToSampleGuacamoleRecipe(eachUom, teapoonUom, tableSpoonUom, dashUom, guacRecipe);

        guacRecipe.getCategories().add(americanCategory);
        guacRecipe.getCategories().add(mexicanCategory);
        guacRecipe.setUrl("http://www.simplyrecipes.com/recipes/perfect_guacamole/");
        guacRecipe.setServings(4);
        guacRecipe.setSource("Simply Recipes");

        log.debug("saving and creating sample guacamole recipe");
        recipeRepository.save(guacRecipe);
    }

    private void addIngredientsToSampleGuacamoleRecipe(UnitOfMeasure eachUom, UnitOfMeasure teapoonUom, UnitOfMeasure tableSpoonUom, UnitOfMeasure dashUom, Recipe guacRecipe) {
        guacRecipe.addIngredient(new Ingredient("ripe avocados", new BigDecimal(2), eachUom));
        guacRecipe.addIngredient(new Ingredient("Kosher salt", new BigDecimal(".5"), teapoonUom));
        guacRecipe.addIngredient(new Ingredient("fresh lime juice or lemon juice", new BigDecimal(2), tableSpoonUom));
        guacRecipe.addIngredient(new Ingredient("minced red onion or thinly sliced green onion", new BigDecimal(2), tableSpoonUom));
        guacRecipe.addIngredient(new Ingredient("serrano chiles, stems and seeds removed, minced", new BigDecimal(2), eachUom));
        guacRecipe.addIngredient(new Ingredient("Cilantro", new BigDecimal(2), tableSpoonUom));
        guacRecipe.addIngredient(new Ingredient("freshly grated black pepper", new BigDecimal(2), dashUom));
        guacRecipe.addIngredient(new Ingredient("ripe tomato, seeds and pulp removed, chopped", new BigDecimal(".5"), eachUom));
    }

    private void createTacosRecipe(UnitOfMeasure tableSpoonUom, UnitOfMeasure teapoonUom, UnitOfMeasure eachUom,
                                      UnitOfMeasure cupsUom, UnitOfMeasure pintUom, Category americanCategory,
                                      Category mexicanCategory) {

        //Yummy Tacos
        Recipe tacosRecipe = new Recipe();
        tacosRecipe.setDescription(SAMPLE_TACO_RECIPE_NAME);
        tacosRecipe.setCookTime(9);
        tacosRecipe.setPrepTime(20);
        tacosRecipe.setDifficulty(Difficulty.MODERATE);

        tacosRecipe.setDirections(getTacoRecipeDirections());

        Notes tacoNotes = new Notes();
        tacoNotes.setRecipeNotes("We have a family motto and it is this: Everything goes better in a tortilla.\n" +
                "Any and every kind of leftover can go inside a warm tortilla, usually with a healthy dose of pickled jalapenos. I can always sniff out a late-night snacker when the aroma of tortillas heating in a hot pan on the stove comes wafting through the house.\n" +
                "Today’s tacos are more purposeful – a deliberate meal instead of a secretive midnight snack!\n" +
                "First, I marinate the chicken briefly in a spicy paste of ancho chile powder, oregano, cumin, and sweet orange juice while the grill is heating. You can also use this time to prepare the taco toppings.\n" +
                "Grill the chicken, then let it rest while you warm the tortillas. Now you are ready to assemble the tacos and dig in. The whole meal comes together in about 30 minutes!\n" +
                "\n" +
                "\n" +
                "Read more: http://www.simplyrecipes.com/recipes/spicy_grilled_chicken_tacos/#ixzz4jvu7Q0MJ");

        tacosRecipe.setNotes(tacoNotes);

        addIngredientsToSampleTacoRecipe(tableSpoonUom, teapoonUom, eachUom, cupsUom, pintUom, tacosRecipe);

        tacosRecipe.getCategories().add(americanCategory);
        tacosRecipe.getCategories().add(mexicanCategory);

        tacosRecipe.setUrl("http://www.simplyrecipes.com/recipes/spicy_grilled_chicken_tacos/");
        tacosRecipe.setServings(4);
        tacosRecipe.setSource("Simply Recipes");

        log.debug("saving and creating sample taco recipe");
        recipeRepository.save(tacosRecipe);
    }

    private void addIngredientsToSampleTacoRecipe(UnitOfMeasure tableSpoonUom, UnitOfMeasure teapoonUom, UnitOfMeasure eachUom, UnitOfMeasure cupsUom, UnitOfMeasure pintUom, Recipe tacosRecipe) {
        tacosRecipe.addIngredient(new Ingredient("Ancho Chili Powder", new BigDecimal(2), tableSpoonUom));
        tacosRecipe.addIngredient(new Ingredient("Dried Oregano", new BigDecimal(1), teapoonUom));
        tacosRecipe.addIngredient(new Ingredient("Dried Cumin", new BigDecimal(1), teapoonUom));
        tacosRecipe.addIngredient(new Ingredient("Sugar", new BigDecimal(1), teapoonUom));
        tacosRecipe.addIngredient(new Ingredient("Salt", new BigDecimal(".5"), teapoonUom));
        tacosRecipe.addIngredient(new Ingredient("Clove of Garlic, Choppedr", new BigDecimal(1), eachUom));
        tacosRecipe.addIngredient(new Ingredient("finely grated orange zestr", new BigDecimal(1), tableSpoonUom));
        tacosRecipe.addIngredient(new Ingredient("fresh-squeezed orange juice", new BigDecimal(3), tableSpoonUom));
        tacosRecipe.addIngredient(new Ingredient("Olive Oil", new BigDecimal(2), tableSpoonUom));
        tacosRecipe.addIngredient(new Ingredient("boneless chicken thighs", new BigDecimal(4), tableSpoonUom));
        tacosRecipe.addIngredient(new Ingredient("small corn tortillas", new BigDecimal(8), eachUom));
        tacosRecipe.addIngredient(new Ingredient("packed baby arugula", new BigDecimal(3), cupsUom));
        tacosRecipe.addIngredient(new Ingredient("medium ripe avocados, slic", new BigDecimal(2), eachUom));
        tacosRecipe.addIngredient(new Ingredient("radishes, thinly sliced", new BigDecimal(4), eachUom));
        tacosRecipe.addIngredient(new Ingredient("cherry tomatoes, halved", new BigDecimal(".5"), pintUom));
        tacosRecipe.addIngredient(new Ingredient("red onion, thinly sliced", new BigDecimal(".25"), eachUom));
        tacosRecipe.addIngredient(new Ingredient("Roughly chopped cilantro", new BigDecimal(4), eachUom));
        tacosRecipe.addIngredient(new Ingredient("cup sour cream thinned with 1/4 cup milk", new BigDecimal(4), cupsUom));
        tacosRecipe.addIngredient(new Ingredient("lime, cut into wedges", new BigDecimal(4), eachUom));
    }

    // below are helper methods to get the directions for specific recipes.
    private String getGuacRecipeDirections() {
        return "1 Cut avocado, remove flesh: Cut the avocados in half. Remove seed. Score the inside of the avocado with a blunt knife and scoop out the flesh with a spoon" +
                "\n" +
                "2 Mash with a fork: Using a fork, roughly mash the avocado. (Don't overdo it! The guacamole should be a little chunky.)" +
                "\n" +
                "3 Add salt, lime juice, and the rest: Sprinkle with salt and lime (or lemon) juice. The acid in the lime juice will provide some balance to the richness of the avocado and will help delay the avocados from turning brown.\n" +
                "Add the chopped onion, cilantro, black pepper, and chiles. Chili peppers vary individually in their hotness. So, start with a half of one chili pepper and add to the guacamole to your desired degree of hotness.\n" +
                "Remember that much of this is done to taste because of the variability in the fresh ingredients. Start with this recipe and adjust to your taste.\n" +
                "4 Cover with plastic and chill to store: Place plastic wrap on the surface of the guacamole cover it and to prevent air reaching it. (The oxygen in the air causes oxidation which will turn the guacamole brown.) Refrigerate until ready to serve.\n" +
                "Chilling tomatoes hurts their flavor, so if you want to add chopped tomato to your guacamole, add it just before serving.\n" +
                "\n" +
                "\n" +
                "Read more: http://www.simplyrecipes.com/recipes/perfect_guacamole/#ixzz4jvpiV9Sd";

    }

    private String getTacoRecipeDirections() {
        return "1 Prepare a gas or charcoal grill for medium-high, direct heat.\n" +
                "2 Make the marinade and coat the chicken: In a large bowl, stir together the chili powder, oregano, cumin, sugar, salt, garlic and orange zest. Stir in the orange juice and olive oil to make a loose paste. Add the chicken to the bowl and toss to coat all over.\n" +
                "Set aside to marinate while the grill heats and you prepare the rest of the toppings.\n" +
                "\n" +
                "\n" +
                "3 Grill the chicken: Grill the chicken for 3 to 4 minutes per side, or until a thermometer inserted into the thickest part of the meat registers 165F. Transfer to a plate and rest for 5 minutes.\n" +
                "4 Warm the tortillas: Place each tortilla on the grill or on a hot, dry skillet over medium-high heat. As soon as you see pockets of the air start to puff up in the tortilla, turn it with tongs and heat for a few seconds on the other side.\n" +
                "Wrap warmed tortillas in a tea towel to keep them warm until serving.\n" +
                "5 Assemble the tacos: Slice the chicken into strips. On each tortilla, place a small handful of arugula. Top with chicken slices, sliced avocado, radishes, tomatoes, and onion slices. Drizzle with the thinned sour cream. Serve with lime wedges.\n" +
                "\n" +
                "\n" +
                "Read more: http://www.simplyrecipes.com/recipes/spicy_grilled_chicken_tacos/#ixzz4jvtrAnNm";
    }
}
