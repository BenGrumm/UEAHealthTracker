package org.example.UEAHealthServer;

import org.example.UEAHealthServer.Exceptions.UserNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // indicates that the data returned by each method will be written straight into the response body instead of rendering a template
public class UserController {

    private final UserRepository repository;

    UserController(UserRepository repo){
        this.repository = repo;
    }

    @GetMapping("/users")
    List<ServerUser> all(){
        return repository.findAll();
    }

    // example curl to add user
    // curl -X POST localhost:8080/users -H 'Content-type:application/json' -d '{"email":"test@gmail.com","firstName":"Ben","surname":"Grummitt","username":"bgrumm","password":"pword","weightStone":0,"weightPounds":0,"height":1.94,"gender":"MALE"}'
    @PostMapping("/users")
    ServerUser newUser(@RequestBody ServerUser newUser){
        return repository.save(newUser);
    }

    @GetMapping("/users/{email}")
    ServerUser oneUser(@PathVariable String email){
        // Throws runtime exception which will be used to render HTTP 404
        return repository.findById(email).orElseThrow(() -> new UserNotFoundException(email));
    }

    @PutMapping("/users/{email}")
    ServerUser replaceUser(@RequestBody ServerUser newUser, @PathVariable String email){
        return repository.findById(email)
                .map(serverUser -> {serverUser.setFirstName(newUser.getFirstName());
                                    serverUser.setSurname(newUser.getSurname());
                                    serverUser.setUsername(newUser.getUsername());
                                    serverUser.setPassword(newUser.getPassword());
                                    serverUser.setHeight(newUser.getHeight());
                                    serverUser.setWeightStone(newUser.getWeightStone());
                                    serverUser.setWeightPounds(newUser.getWeightPounds());
                                    serverUser.setGender(newUser.getGender());
                                    return repository.save(serverUser);})
                .orElseGet(()-> {newUser.setEmail(email);
                                return repository.save(newUser);
                });
    }

    @DeleteMapping("/users/{email}")
    void deleteUser(@PathVariable String email){
        repository.deleteById(email);
    }

}
