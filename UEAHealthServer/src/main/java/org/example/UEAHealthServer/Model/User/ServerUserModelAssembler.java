package org.example.UEAHealthServer.Model.User;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ServerUserModelAssembler implements RepresentationModelAssembler<ServerUser, EntityModel<ServerUser>> {

    @Override
    public EntityModel<ServerUser> toModel(ServerUser entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(UserController.class).oneUser(entity.getEmail())).withSelfRel(),
                linkTo(methodOn(UserController.class).all()).withRel("users"));
    }
}
