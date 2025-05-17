package com.spring.rms.repositories;

import com.spring.rms.models.ResourceBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface IResourceBookRepository extends JpaRepository<ResourceBooking, UUID> {
    List<ResourceBooking> findByUserId(UUID userId);
    List<ResourceBooking> findByResourceId(UUID resourceId);
}