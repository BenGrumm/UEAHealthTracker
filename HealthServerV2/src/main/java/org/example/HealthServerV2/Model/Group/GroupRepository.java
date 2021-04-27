package org.example.HealthServerV2.Model.Group;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "groups", path = "groups")
public interface GroupRepository extends JpaRepository<UserGroup, String> {

    UserGroup findByInviteCode(@Param("inviteCode") String inviteCode);

}
