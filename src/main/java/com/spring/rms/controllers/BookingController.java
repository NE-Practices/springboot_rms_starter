package com.spring.rms.controllers;


import com.spring.rms.models.ResourceBooking;
import com.spring.rms.payload.response.ApiResponse;
import com.spring.rms.services.IResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final IResourceService resourceService;
    private final JavaMailSender mailSender;

    @PostMapping("/{id}/approve")
    @Operation(summary = "Approve a booking", description = "Allows ADMIN to approve a booking", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ApiResponse> approveBooking(@PathVariable UUID id, Authentication authentication) {
        ResourceBooking booking = resourceService.approveBooking(id, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Booking approved", booking));
    }

    @PostMapping("/{id}/reject")
    @Operation(summary = "Reject a booking", description = "Allows ADMIN to reject a booking", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ApiResponse> rejectBooking(@PathVariable UUID id, Authentication authentication) {
        ResourceBooking booking = resourceService.rejectBooking(id, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Booking rejected", booking));
    }

    @GetMapping("/all")
    @Operation(summary = "Get bookings", description = "Retrieve bookings (own for USER)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ApiResponse> getBookings(){
        List<ResourceBooking> bookings = resourceService.getBookings();
        return ResponseEntity.ok(ApiResponse.success("Bookings", bookings));
    }

    @GetMapping
    @Operation(summary = "Get bookings", description = "Retrieve bookings (all for ADMIN)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ApiResponse> getMyBookings(Authentication authentication) {
        List<ResourceBooking> bookings = resourceService.getMyBookings(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Bookings retrieved", bookings));
    }

}