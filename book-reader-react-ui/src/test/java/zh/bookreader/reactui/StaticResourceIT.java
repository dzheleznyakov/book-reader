package zh.bookreader.reactui;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class StaticResourceIT {
    private static final String UI_PAGE_RESOURCE = "index.html";

    @Autowired
    MockMvc mockMvc;

    @Test
    void testLoadMainUIPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl(UI_PAGE_RESOURCE));
    }

    @Disabled
    @Test
    void testLoadNonExistingUIPage() throws Exception {
        mockMvc.perform(get("/non/existing/path"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl(UI_PAGE_RESOURCE));
    }
}
