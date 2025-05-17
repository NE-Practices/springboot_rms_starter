package com.spring.rms.services.serviceImpls;

import com.spring.rms.enums.EBookStatus;
import com.spring.rms.models.Resource;
import com.spring.rms.models.ResourceBooking;
import com.spring.rms.models.User;
import com.spring.rms.payload.request.CreateResourceDTO;
import com.spring.rms.payload.request.UpdateResourceDTO;
import com.spring.rms.repositories.IResourceBookRepository;
import com.spring.rms.repositories.IResourceRepository;
import com.spring.rms.repositories.IUserRepository;
import com.spring.rms.services.IMailService;
import com.spring.rms.services.IResourceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements IResourceService {

    private final IResourceRepository resourceRepository;
    private final IUserRepository userRepository;
    private final IResourceBookRepository resourceBookingRepository;
    private final IMailService mailService;
    private static final Logger logger = LoggerFactory.getLogger(ResourceServiceImpl.class);

    @Override
    public Resource createResource(CreateResourceDTO dto, String username) {
        Resource resource = new Resource();
        resource.setName(dto.getName());
        resource.setDescription(dto.getDescription());
        resource.setCategory(dto.getCategory());

        Resource savedResource = resourceRepository.save(resource);
        mailService.sendResourceCreatedEmail(username, savedResource);
        return savedResource;
    }

    @Override
    public Resource updateResource(UUID id, UpdateResourceDTO dto, String username) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resource not found"));
        resource.setName(dto.getName());
        resource.setDescription(dto.getDescription());
        resource.setCategory(dto.getCategory());

        Resource updatedResource = resourceRepository.save(resource);
        mailService.sendResourceUpdatedEmail(username, updatedResource);
        return updatedResource;
    }

    @Override
    public void deleteResource(UUID id, String username) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resource not found"));
        resourceRepository.delete(resource);
        mailService.sendResourceDeletedEmail(username, resource);
    }

    @Override
    public Resource getResourceById(UUID id) {
        return resourceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resource not found"));
    }

    @Transactional
    @Override
    public ResourceBooking bookResource(UUID resourceId, String username) {
        try {
            logger.info("Starting resource booking for user: {}", username);

            Resource resource = resourceRepository.findById(resourceId)
                    .orElseThrow(() -> new RuntimeException("Resource not found: " + resourceId));
            logger.info("Resource found: {}", resource.getId());

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found: " + username));
            logger.info("User found: {}", user.getUsername());

            ResourceBooking booking = new ResourceBooking();
            booking.setUser(user);
            booking.setResource(resource);
            booking.setStatus(EBookStatus.PENDING);
            booking.setCreatedAt(LocalDateTime.now());
            booking.setUpdatedAt(LocalDateTime.now());

            ResourceBooking savedBooking = resourceBookingRepository.save(booking);
            logger.info("Booking saved: {}", savedBooking.getId());

            // Notify user
            mailService.sendBookingStatusEmail(user.getUsername(), resource, "submitted", "PENDING");
            logger.info("User notification sent to: {}", user.getEmail());

            // Notify admin (use default email if admin user not found)
            try {
                User admin = userRepository.findByUsername("admin")
                        .orElse(null);
                if (admin != null) {
                    mailService.sendBookingStatusEmail(admin.getUsername(), resource, "new booking", "PENDING");
                    logger.info("Admin notification sent to: {}", admin.getEmail());
                } else {
                    mailService.sendBookingStatusEmailToDefault(resource, "new booking", "PENDING");
                    logger.info("Admin user not found, sent notification to default email");
                }
            } catch (Exception e) {
                logger.error("Failed to send admin notification: {}", e.getMessage());
                // Continue execution, don’t fail booking due to admin notification error
            }

            return savedBooking;

        } catch (Exception e) {
            logger.error("Error during resource booking: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    @Override
    public ResourceBooking approveBooking(UUID bookingId, String adminUsername) {
        ResourceBooking booking = resourceBookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        User admin = userRepository.findByUsername(adminUsername)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        booking.setStatus(EBookStatus.APPROVED);
        booking.setUpdatedAt(LocalDateTime.now());
        ResourceBooking updatedBooking = resourceBookingRepository.save(booking);

        // Notify user
        mailService.sendBookingStatusEmail(booking.getUser().getUsername(), booking.getResource(), "approved", "APPROVED");

        return updatedBooking;
    }

    @Transactional
    @Override
    public ResourceBooking rejectBooking(UUID bookingId, String adminUsername) {
        ResourceBooking booking = resourceBookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        User admin = userRepository.findByUsername(adminUsername)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        booking.setStatus(EBookStatus.REJECTED);
        booking.setUpdatedAt(LocalDateTime.now());
        ResourceBooking updatedBooking = resourceBookingRepository.save(booking);

        // Notify user
        mailService.sendBookingStatusEmail(booking.getUser().getUsername(), booking.getResource(), "rejected", "REJECTED");

        return updatedBooking;
    }

    @Override
    public List<ResourceBooking> getMyBookings(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getRoles().stream().anyMatch(role -> role.getName().equals("ADMIN"))) {
            return resourceBookingRepository.findAll();
        }
        return resourceBookingRepository.findByUserId(user.getId());
    }

    @Override
    public List<ResourceBooking> getBookings() {
        return resourceBookingRepository.findAll();
    }

    @Override
    public Page<Resource> getAllResources(Pageable pageable) {
        return resourceRepository.findAll(pageable);
    }

    @Override
    public Page<Resource> searchResources(String searchKey, Pageable pageable) {
        return resourceRepository.findByNameContainingIgnoreCase(searchKey, pageable);
    }
}