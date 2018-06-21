package tran.example.recipeapp.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tran.example.recipeapp.domain.Recipe;
import tran.example.recipeapp.repositories.RecipeRepository;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {
    private final RecipeRepository recipeRepository;

    @Autowired
    public ImageServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    @Transactional
    public void saveImageFile(Long recipeId, MultipartFile file) {
        try {
            Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
            if(recipeOptional.isPresent()) {
                Recipe recipe = recipeOptional.get();
                /**
                 * note: Hibernate recommends to use a Wrapper type (not the primitive) although using byte[] can work
                 * if the file is null this would cause issues.
                  */
                int fileByteIndex = 0;
                Byte[] copiedFileContents = new Byte[file.getBytes().length];
                for(byte fileContent : file.getBytes()) {
                    copiedFileContents[fileByteIndex++] = fileContent;
                }
                recipe.setImage(copiedFileContents);
                recipeRepository.save(recipe);
                log.debug("Received a file");
            }
        } catch (IOException e) {
            log.debug("error occurred while parsing the file!");
            e.printStackTrace();
        }
    }
}
