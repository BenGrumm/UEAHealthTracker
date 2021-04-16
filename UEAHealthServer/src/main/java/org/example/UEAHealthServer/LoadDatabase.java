package org.example.UEAHealthServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    // Spring Boot will run all command line runner beans once application context is loaded
    @Bean
    CommandLineRunner initDatabase(UserRepository repository){
        return args -> {
            // Log saving of new data to database
            log.info("Preloading" + repository.save(new ServerUser("James", "Earl", "JEJones", "JEJ@gmail.com", "empireDidNothingWrong"
                    , 1.7, 9, 8, "Male")));

            log.info("Preloading" + repository.save(new ServerUser("Jessica", "Berry", "JB", "JBerry@gmail.com", "blueBerry"
                    , 1.5, 8, 4, "Female")));
        };
    }

}
