package com.codegym.Controller;

import com.codegym.Model.Category;
import com.codegym.Service.Category.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private Environment environment;
    @Autowired
    private ICategoryService categoryService;

    @GetMapping("")
    public String showList(Model model){
        model.addAttribute("list", categoryService.findAll());
        return "categories/list";
    }

    @GetMapping("/create")
    public String showFormCreate(Model model){
        model.addAttribute("categories", new Category());
        return "/categories/create";
    }

    @PostMapping("/create")
    public String createCategories(@ModelAttribute("categories") Category categoryForm){
        Category category = new Category(categoryForm.getName());
        MultipartFile file = categoryForm.getAva();
        String image = file.getOriginalFilename();
        category.setImg(image);
        String fileUpload = environment.getProperty("file_upload").toString();
        try {
            FileCopyUtils.copy(file.getBytes(), new File(fileUpload + image));
        } catch (IOException e) {
            e.printStackTrace();
        }
        categoryService.save(category);
        return "/categories/create";
    }
}
