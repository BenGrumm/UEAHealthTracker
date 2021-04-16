package org.example.UEAHealthServer;

import org.springframework.data.jpa.repository.JpaRepository;

interface UserRepository extends JpaRepository<ServerUser, String>{
}
