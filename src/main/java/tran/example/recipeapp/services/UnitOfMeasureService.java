package tran.example.recipeapp.services;

import tran.example.recipeapp.commands.UnitOfMeasureCommand;
import tran.example.recipeapp.domain.UnitOfMeasure;

import java.util.Set;

public interface UnitOfMeasureService {
    Set<UnitOfMeasureCommand> getUnitOfMeasures();
}
