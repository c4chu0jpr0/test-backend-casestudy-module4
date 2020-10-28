package com.codegym.Controller;

import com.codegym.Model.Category;
import com.codegym.Model.Song;
import com.codegym.Service.Category.CategoryService;
import com.codegym.Service.Song.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/songs")
public class SongController {
    @Autowired
    private Environment environment;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SongService songService;

    @ModelAttribute("categories")
    private Iterable<Category> categoryList(){
        Iterable<Category> all = categoryService.findAll();
        return all;
    }

    @GetMapping("")
    public String listSong(Model model){
        model.addAttribute("list",songService.findAll());
        return "songs/list";
    }

    @GetMapping("/create")
    public String showFormCreate(Model model){
        model.addAttribute("song", new Song());
        return "/songs/create";
    }
    @PostMapping("/create")
    public String createSong(@ModelAttribute("song") Song songForm){
        Song song = new Song(songForm.getName(), songForm.getSinger(), songForm.getProducer(), songForm.getDescription(), songForm.getCategory());
        MultipartFile file = songForm.getMp3();
        String mp3 = file.getOriginalFilename();
        song.setLinkMp3(mp3);
        String fileUpload = environment.getProperty("file_upload").toString();
        try {
            FileCopyUtils.copy(file.getBytes(), new File(fileUpload + mp3));
        } catch (IOException e) {
            e.printStackTrace();
        }
        songService.save(song);
        return "redirect:/songs";
    }
}
