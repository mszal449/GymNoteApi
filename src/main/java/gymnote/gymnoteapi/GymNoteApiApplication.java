package gymnote.gymnoteapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "gymnote.gymnoteapi.repository")
@EntityScan(basePackages = "gymnote.gymnoteapi.entity")
public class GymNoteApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(GymNoteApiApplication.class, args);
    }
}