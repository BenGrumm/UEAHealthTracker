package org.example.UEAHealthServer.Model.UserGroupManyToMany;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInGroupRepository extends JpaRepository<UserInGroup, Integer> {
}
