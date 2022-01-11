package ua.lysenko.andrii.zip.web;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(MyController.class)
public class ZipWebMvcTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StorageService storageService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testUploadViewGet() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/uploadForm"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("uploadForm"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers.containsString("Upload a file:")));
    }

    @Test
    public void testUploadPost() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
                        .file("myFile", new byte[1024]))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/uploadForm"));
    }

}
