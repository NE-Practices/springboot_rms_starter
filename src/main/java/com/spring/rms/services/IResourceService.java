package com.spring.rms.services;


import com.spring.rms.models.Resource;
import com.spring.rms.models.ResourceBooking;
import com.spring.rms.payload.request.CreateResourceDTO;
import com.spring.rms.payload.request.UpdateResourceDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IResourceService {
    Resource createResource(CreateResourceDTO dto, String username);
    Resource updateResource(UUID id, UpdateResourceDTO dto, String username);
    void deleteResource(UUID id, String username);
    Resource getResourceById(UUID id);
    Page<Resource> getAllResources(Pageable pageable);
    Page<Resource> searchResources(String searchKey, Pageable pageable);
    ResourceBooking bookResource(UUID resourceId, String username);
    ResourceBooking approveBooking(UUID bookingId, String adminUsername);
    ResourceBooking rejectBooking(UUID bookingId, String adminUsername);
    List<ResourceBooking> getMyBookings(String username);
    List<ResourceBooking> getBookings();
}