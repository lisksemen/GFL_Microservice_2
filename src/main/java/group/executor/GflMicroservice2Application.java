package group.executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@SpringBootApplication
public class GflMicroservice2Application {

    public static void main(String[] args) {
        SpringApplication.run(GflMicroservice2Application.class, args);
    }

}
