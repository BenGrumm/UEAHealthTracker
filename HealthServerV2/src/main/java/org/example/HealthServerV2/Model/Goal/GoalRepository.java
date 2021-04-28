package org.example.HealthServerV2.Model.Goal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "goals", path = "goals")
public interface GoalRepository extends JpaRepository<Goal, Integer> {
}
