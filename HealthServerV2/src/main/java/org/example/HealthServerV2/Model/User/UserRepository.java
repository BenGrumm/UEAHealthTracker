package org.example.HealthServerV2.Model.User;

import org.example.HealthServerV2.Model.User.ServerUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface UserRepository extends JpaRepository<ServerUser, String> {
}
