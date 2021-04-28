package org.example.HealthServerV2.Controller;

import org.example.HealthServerV2.Model.Group.GroupRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/activities")
public class GroupControl {

    private final GroupRepository repository;

    GroupControl(GroupRepository repository){
        this.repository = repository;
    }

//    @PostMapping("/{id}")
//    ResponseEntity addExercise(@PathVariable int id, @RequestBody Exercise exercise){
//        UserGroup group = repository.findById(id).orElseThrow(() -> new GroupNotFoundException(id));
//
//        group.addExercise(exercise);
//
//        return ResponseEntity.ok().build();
//    }

}
