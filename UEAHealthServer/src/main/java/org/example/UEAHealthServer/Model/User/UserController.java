package org.example.UEAHealthServer.Model.User;

import org.example.UEAHealthServer.Exceptions.UserNotFoundException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController // indicates that the data returned by each method will be written straight into the response body instead of rendering a template
public class UserController {

    private final UserRepository repository;
    private final ServerUserModelAssembler assembler;

    UserController(UserRepository repo, ServerUserModelAssembler assembler){
        this.repository = repo;
        this.assembler = assembler;
    }

    /**
     * List all user currently stored in the database
     * @return rjson format of all users
     */
    @GetMapping("/users")
    CollectionModel<EntityModel<ServerUser>> all(){
        List<EntityModel<ServerUser>> users = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(users, linkTo(methodOn(UserController.class).all()).withSelfRel());
    }

    // example curl to add user
    // curl -X POST localhost:8080/users -H 'Content-type:application/json' -d '{"email":"test@gmail.com","firstName":"Ben","surname":"Grummitt","username":"bgrumm","password":"pword","weightStone":0,"weightPounds":0,"height":1.94,"gender":"MALE"}'
    /**
     * Add a new user to the db by putting the json format of data in request body
     * @param newUser Auto converted user from json data
     * @return Same json data is successful
     */
    @PostMapping("/users")
    EntityModel<ServerUser> newUser(@RequestBody ServerUser newUser){
        ServerUser user = repository.save(newUser);
        return assembler.toModel(user);
    }

    /**
     * Get User From Mapping /users/{email}, email is the unique id
     * Email is then passed to the function and the repository is searched. If the user
     * is not found then a user not found exception is thrown.
     * @param email of user to retrieve from db
     * @return json of data or error if not found
     */
    @GetMapping("/users/{email}")
    EntityModel<ServerUser> oneUser(@PathVariable String email){
        // Throws runtime exception which will be used to render HTTP 404
        ServerUser user = repository.findById(email).orElseThrow(() -> new UserNotFoundException(email));
        return assembler.toModel(user);
    }

    /**
     * This can be used to update a user - This will completely replace the data so it must be complete in json body
     * @param newUser Converted from json data to user
     * @param email in the url path
     * @return user json data returned
     */
    @PutMapping("/users/{email}")
    EntityModel<ServerUser> replaceUser(@RequestBody ServerUser newUser, @PathVariable String email){
        ServerUser user = repository.findById(email)
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

        return assembler.toModel(user);
    }

    /**
     * Delete user from db
     * @param email id of user to delete
     */
    @DeleteMapping("/users/{email}")
    void deleteUser(@PathVariable String email){
        repository.deleteById(email);
    }

}
