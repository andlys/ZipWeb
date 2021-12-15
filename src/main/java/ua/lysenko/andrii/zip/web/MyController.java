package ua.lysenko.andrii.zip.web;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.lysenko.andrii.zip.Zipper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequestMapping
public class MyController {

    private Resource resource;
    private Path uploadedFilePath;
    private Path baseDir = Paths.get("D:\\files\\");

    @GetMapping(path = "uploadForm")
    public String uploadForm(Model model) {
        model.addAttribute("zipFiles", List.of("asdasd", "bbbb", "lorem ipsum.zip", "output.zip")
                .stream().map( str -> "/downloadZip/" + str).collect(Collectors.toList()));
        return "uploadForm";
    }

    @PostMapping(path = "upload")
    public String upload(@RequestParam("myFile") MultipartFile myFile, RedirectAttributes redirectAttributes) throws IOException {
        resource = myFile.getResource();
        uploadedFilePath = Paths.get(baseDir.toString() + resource.getFilename());
        Files.write(uploadedFilePath, resource.getInputStream().readAllBytes());
        String fileSizeMsg = myFile.getSize() / 1024 + "KB";
        log.info(fileSizeMsg);
        log.info("Uploaded a file");
        redirectAttributes.addFlashAttribute("message",
                String.format("Success uploading a file '%s' of size ", myFile.getOriginalFilename(), fileSizeMsg));
        redirectAttributes.addFlashAttribute("file", uploadedFilePath.getFileName());
        return "redirect:/uploadForm";
    }

    @GetMapping("/download")
    @ResponseBody
    public ResponseEntity<Resource> download() {
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
    }

    @GetMapping("/downloadZip")
    @ResponseBody
    public ResponseEntity<Resource> downloadZip() throws FileNotFoundException {
        Path outputZip = Paths.get(uploadedFilePath.getParent().toString(), FilenameUtils.getBaseName(uploadedFilePath.getFileName().toString()) + ".zip");
        Zipper.zip(uploadedFilePath.toString(), outputZip.toString());
        Resource resourceZipped = new InputStreamResource(new FileInputStream(outputZip.toFile()));
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + outputZip.getFileName().toString() + "\"").body(resourceZipped);
    }

    @GetMapping("/downloadZip/{fileName:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadZip(@PathVariable String fileName) throws FileNotFoundException {
        log.info(fileName);
        Path outputZip = Paths.get(baseDir.toString(), FilenameUtils.getBaseName(fileName) + ".zip");
        // suppose that file is already zipped
        //Zipper.zip(uploadedFilePath.toString(), outputZip.toString());
        Resource resourceZipped = new InputStreamResource(new FileInputStream(outputZip.toFile()));
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + outputZip.getFileName().toString() + "\"").body(resourceZipped);
    }
}
