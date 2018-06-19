package tran.example.recipeapp.repositories;

import org.springframework.data.repository.CrudRepository;
import tran.example.recipeapp.domain.UnitOfMeasure;

public interface UnitOfMeasureRepository extends CrudRepository<UnitOfMeasure, Long> {
}
