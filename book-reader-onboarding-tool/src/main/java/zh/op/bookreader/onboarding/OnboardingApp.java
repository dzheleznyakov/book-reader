package zh.op.bookreader.onboarding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({"zh.bookreader", "zh.op.bookreader"})
@EnableJpaRepositories("zh.bookreader.data.repositories")
@EntityScan("zh.bookreader.data.model")
public class OnboardingApp {
    public static void main(String[] args) {
        SpringApplication.run(OnboardingApp.class, args);
    }
}
