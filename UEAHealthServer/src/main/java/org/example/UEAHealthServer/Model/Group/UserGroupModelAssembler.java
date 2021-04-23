package org.example.UEAHealthServer.Model.Group;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class UserGroupModelAssembler implements RepresentationModelAssembler<UserGroup, EntityModel<UserGroup>> {

    @Override
    public EntityModel<UserGroup> toModel(UserGroup entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(GroupController.class).oneGroup(entity.getId())).withSelfRel(),
                linkTo(methodOn(GroupController.class).all()).withRel("groups"));
    }
}
