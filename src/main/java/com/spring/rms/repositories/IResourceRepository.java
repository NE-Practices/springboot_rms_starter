package com.spring.rms.repositories;

import com.spring.rms.models.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IResourceRepository extends JpaRepository<Resource, UUID> {
    Page<Resource> findByNameContainingIgnoreCase(String name, Pageable pageable);
}