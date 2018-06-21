package tran.example.recipeapp.controllers;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import tran.example.recipeapp.commands.RecipeCommand;
import tran.example.recipeapp.services.ImageService;
import tran.example.recipeapp.services.RecipeService;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class ImageController {

    private final ImageService imageService;
    private final RecipeService recipeService;

    public ImageController(ImageService imageService, RecipeService recipeService) {
        this.imageService = imageService;
        this.recipeService = recipeService;
    }

    @GetMapping(RecipeController.RECIPE_BASE_URL + "/{id}/image")
    public String showUploadForm(@PathVariable String id, Model model){
        model.addAttribute("recipe", recipeService.findCommandById(Long.valueOf(id)));

        return "recipe/imageuploadform";
    }

    @PostMapping(RecipeController.RECIPE_BASE_URL + "/{id}/image")
    public String handleImagePost(@PathVariable String id, @RequestParam("imagefile") MultipartFile file){

        imageService.saveImageFile(Long.valueOf(id), file);

        return "redirect:/recipe/" + id + "/show";
    }

    @GetMapping(RecipeController.RECIPE_BASE_URL + "/{id}/recipeimage")
    public void renderImageFromDB(@PathVariable String id, HttpServletResponse response) throws IOException {
        RecipeCommand recipeCommand = recipeService.findCommandById(Long.valueOf(id));

        if(recipeCommand.getImage() != null) {
            byte[] imageContents = new byte[recipeCommand.getImage().length];
            int fileByteIndex = 0;
            for(byte fileContent : recipeCommand.getImage()) {
                imageContents[fileByteIndex++] = fileContent;
            }

            response.setContentType("image/jpeg");
            InputStream inputStream = new ByteArrayInputStream(imageContents);
            IOUtils.copy(inputStream, response.getOutputStream());
        }
    }
}
