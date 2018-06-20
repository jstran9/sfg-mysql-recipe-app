package tran.example.recipeapp.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(exclude = "recipe")
@Entity
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private BigDecimal amount;

    // one to one and always get the unit of measure when we load this individual ingredient.
    // this is the default fetch but we want to specify it to be clear that we always want to grab a UoM.
    @OneToOne(fetch = FetchType.EAGER)
    private UnitOfMeasure uom;

    // no cascade b/c we don't want to cascade upward to delete the recipe.
    // we want to freely delete ingredients assigned to the recipe.
    @ManyToOne
    private Recipe recipe;

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Ingredient() {}

    public Ingredient(String description, BigDecimal amount, UnitOfMeasure uom) {
        this.description = description;
        this.amount = amount;
        this.uom = uom;
    }

    public Ingredient(String description, BigDecimal amount, UnitOfMeasure uom, Recipe recipe) {
        this.description = description;
        this.amount = amount;
        this.uom = uom;
        this.recipe = recipe;
    }
}
