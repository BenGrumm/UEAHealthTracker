package org.example.HealthServerV2.Controller;

import org.apache.catalina.Server;
import org.example.HealthServerV2.Model.Group.GroupNotFoundException;
import org.example.HealthServerV2.Model.Group.GroupRepository;
import org.example.HealthServerV2.Model.Group.UserGroup;
import org.example.HealthServerV2.Model.User.ServerUser;
import org.example.HealthServerV2.Model.User.UserNotFoundException;
import org.example.HealthServerV2.Model.User.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@RestController
@RequestMapping("/")
public class Controller {

    UserRepository users;
    GroupRepository groups;

    public Controller(UserRepository users, GroupRepository groups){
        this.users = users;
        this.groups = groups;
    }

    @PutMapping("/invite/{groupName}/{email}")
    ResponseEntity inviteToGroup(@PathVariable String groupName, @PathVariable String email){
        return ResponseEntity.ok().build();
    }

    @PutMapping("/email/{groupName}/{email}/{id}")
    ResponseEntity sendEmailToGroup(@PathVariable String groupName, @PathVariable String email, @PathVariable int id){
        ServerUser userWhoCompleted = users.findById(email).orElseThrow(() -> new UserNotFoundException(email));
        UserGroup groupToEmail = groups.findById(groupName).orElseThrow(() -> new GroupNotFoundException(groupName));

        for(ServerUser user : groupToEmail.getUsers()){
            try {
                Email.sendEmail("User In Your Group Completed Goal", "Someone completed a goal fuck knows", user.getEmail());
            }catch (MessagingException e){
                e.printStackTrace();
            }
        }

        return ResponseEntity.ok().build();
    }


}
