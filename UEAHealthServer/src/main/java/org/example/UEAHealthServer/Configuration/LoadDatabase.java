package org.example.UEAHealthServer.Configuration;

import org.apache.catalina.User;
import org.example.UEAHealthServer.Model.User.ServerUser;
import org.example.UEAHealthServer.Model.User.UserRepository;
import org.example.UEAHealthServer.Model.UserInGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    // Spring Boot will run all command line runner beans once application context is loaded
    @Bean
    CommandLineRunner initDatabase(UserRepository repository){
        return args -> {
            // Log saving of new data to database
            log.info("Preloading" + repository.save(new ServerUser("James", "Earl", "JEJones", "JEJ@gmail.com", "empireDidNothingWrong"
                    , 1.7, 9, 8, "Male", new HashSet<>())));

            log.info("Preloading" + repository.save(new ServerUser("Jessica", "Berry", "JB", "JBerry@gmail.com", "blueBerry"
                    , 1.5, 8, 4, "Female", new HashSet<>())));
        };
    }

}
