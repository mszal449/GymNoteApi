package gymnote.gymnoteapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "gymnote.gymnoteapi.repository")
public class GymNoteApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(GymNoteApiApplication.class, args);
    }

}
