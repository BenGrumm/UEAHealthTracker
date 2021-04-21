package org.example.UEAHealthServer.Model.User;

import org.example.UEAHealthServer.Model.User.ServerUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<ServerUser, String>{
}
