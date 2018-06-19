package tran.example.recipeapp.repositories;

import org.springframework.data.repository.CrudRepository;
import tran.example.recipeapp.domain.Category;

public interface CategoryRepository extends CrudRepository<Category, Long> {
}
