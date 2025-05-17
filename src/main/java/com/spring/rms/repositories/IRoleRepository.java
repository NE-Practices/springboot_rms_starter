package com.spring.rms.repositories;

import com.spring.rms.enums.ERole;
import com.spring.rms.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IRoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(ERole name);
}