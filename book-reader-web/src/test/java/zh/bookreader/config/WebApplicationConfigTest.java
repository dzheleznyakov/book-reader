package zh.bookreader.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest
class WebApplicationConfigTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    void name() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andDo(result -> {
                    System.out.println("result = " + result);
                });
    }
}