package zh.bookreader.reactui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping({"/", "/books"})
    public String index() {
        return "index.html";
    }
}
