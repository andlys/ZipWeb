package ua.lysenko.andrii.zip.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequestMapping
public class MyController {

    private StorageService storageService;

    @Autowired
    public MyController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping(path = "uploadForm")
    public String uploadForm(Model model) throws IOException {
        model.addAttribute("zipFiles",
                storageService.getAllFiles()
                        .map(path -> "downloadZip/" + path.toString()).collect(Collectors.toList()));
        return "uploadForm";
    }

    @PostMapping(path = "upload")
    public String upload(@RequestParam("myFile") MultipartFile myFile, RedirectAttributes redirectAttributes) throws IOException {
        storageService.storeFile(myFile);
        String fileSizeMsg = myFile.getSize() / 1024 + "KB";
        log.info(fileSizeMsg);
        log.info("Uploaded a file");
        redirectAttributes.addFlashAttribute("message",
                String.format("Success uploading a file '%s' of size %s", myFile.getOriginalFilename(), fileSizeMsg));
        redirectAttributes.addFlashAttribute("file", myFile.getOriginalFilename());
        return "redirect:/uploadForm";
    }

    @GetMapping("/downloadZip/{fileName:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadZip(@PathVariable String fileName) throws FileNotFoundException {
        log.info(fileName);
        Resource resourceZipped = storageService.getFile(fileName);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + fileName + "\"").body(resourceZipped);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(FileNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

}
