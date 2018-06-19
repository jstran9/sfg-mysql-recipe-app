package tran.example.recipeapp.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = "recipes")
@Entity
/**
 * https://www.udemy.com/spring-framework-5-beginner-to-guru/learn/v4/questions/3622292
 */
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;

    @ManyToMany(mappedBy = "categories")
    private Set<Recipe> recipes;

}
