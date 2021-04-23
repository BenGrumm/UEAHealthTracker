package org.example.UEAHealthServer.Model.Group;

import org.apache.catalina.User;
import org.example.UEAHealthServer.Exceptions.GroupNotFoundException;
import org.example.UEAHealthServer.Model.User.ServerUser;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class GroupController {

    private final GroupRepository repository;
    private final UserGroupModelAssembler assembler;

    GroupController(GroupRepository repository, UserGroupModelAssembler assembler){
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/groups")
    CollectionModel<EntityModel<UserGroup>> all(){
        List<EntityModel<UserGroup>> groups = repository.findAll().stream().
                map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(groups, linkTo(methodOn(GroupController.class).all()).withSelfRel());
    }


    @PostMapping("/groups")
    EntityModel<UserGroup> newGroup(@RequestBody UserGroup newGroup){
        UserGroup group = repository.save(newGroup);
        return assembler.toModel(group);
    }

    @GetMapping("/groups/{id}")
    EntityModel<UserGroup> oneGroup(@PathVariable int id){
        UserGroup userGroup = repository.findById(id).orElseThrow(() -> new GroupNotFoundException(id));
        return assembler.toModel(userGroup);
    }

    @PutMapping("/groups/{id}")
    EntityModel<UserGroup> replaceUser(@RequestBody UserGroup updatedGroup, @PathVariable int id){
        UserGroup group = repository.findById(id)
                .map(userGroup -> {
                    userGroup.setDescription(updatedGroup.getDescription());
                    userGroup.setName(updatedGroup.getName());
                    return repository.save(userGroup); })
                .orElseGet(()->{updatedGroup.setId(id);
                return repository.save(updatedGroup);
                });

        return assembler.toModel(group);
    }

    @DeleteMapping("/groups/{id}")
    void deleteGroup(@PathVariable int id){
        repository.deleteById(id);
    }
}
