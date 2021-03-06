package ua.lysenko.andrii.zip.web;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.stream.Collectors;

@Controller
@RequestMapping
public class MyController {

    private Logger log = LoggerFactory.getLogger(MyController.class);

    private final StorageService storageService;

    @Autowired
    public MyController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping(path = "uploadForm")
    public String uploadForm(Model model) throws IOException {
        storageService.init();
        model.addAttribute("zipFiles",
                storageService.getAllFiles().stream()
                        .map(path -> "download/zip/" + path.toString()).collect(Collectors.toList()));
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

    @GetMapping("/download/zip/{fileName:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadZip(@PathVariable String fileName) throws IOException {
        log.info(fileName);
        Resource resourceZipped = storageService.getFile(fileName);
        String fileNameZip = FilenameUtils.getBaseName(fileName) + ".zip";
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + fileNameZip + "\"").body(resourceZipped);
    }

    @GetMapping("/download/zipAll")
    @ResponseBody
    public ResponseEntity<Resource> downloadZipAll() throws IOException {
        log.info("zip all files");
        Resource resourceZipped = storageService.getAllFilesZip();
        String fileNameZip = "all_files_" + System.currentTimeMillis() + ".zip";
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + fileNameZip + "\"").body(resourceZipped);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(FileNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

}
