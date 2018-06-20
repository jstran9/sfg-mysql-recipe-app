package tran.example.recipeapp.repositories;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import tran.example.recipeapp.domain.UnitOfMeasure;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest // bring up an embedded database and configures spring data JPA.
public class UnitOfMeasureRepositoryTestIT {

    private final String teaspoonUnit = "Teaspoon";
    private final String getTeaspoonUnitTypo = "Teaspoons";
    private final String cupUnit = "Cup";
    @Autowired
    UnitOfMeasureRepository unitOfMeasureRepository;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void findByDescription() throws Exception {
        Optional<UnitOfMeasure> unitOfMeasure = unitOfMeasureRepository.findByDescription(teaspoonUnit);
        assertEquals(teaspoonUnit, unitOfMeasure.get().getDescription());
        assertNotEquals(getTeaspoonUnitTypo, unitOfMeasure.get().getDescription());
    }

    @Test
    public void findByDescriptionCup() throws Exception {
        Optional<UnitOfMeasure> unitOfMeasureOptional = unitOfMeasureRepository.findByDescription(cupUnit);
        assertEquals(cupUnit, unitOfMeasureOptional.get().getDescription());
    }
}