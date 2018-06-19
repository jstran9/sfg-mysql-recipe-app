package tran.example.recipeapp.repositories;

import org.springframework.data.repository.CrudRepository;
import tran.example.recipeapp.domain.Category;

import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Category, Long> {
    Optional<Category> findByDescription(String description);
}
