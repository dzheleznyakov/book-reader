package zh.bookreader.reactui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"zh.bookreader.api", "zh.bookreader.reactui", "zh.bookreader.services"})
public class ReactUiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReactUiApplication.class, args);
    }
}
