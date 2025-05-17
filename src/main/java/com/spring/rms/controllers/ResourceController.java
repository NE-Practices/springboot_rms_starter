package com.spring.rms.controllers;


import com.spring.rms.payload.request.CreateResourceDTO;
import com.spring.rms.payload.request.UpdateResourceDTO;
import com.spring.rms.payload.response.ApiResponse;
import com.spring.rms.services.IResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final IResourceService resourceService;

    @PostMapping
    @Operation(
            summary = "Create a new resource",
            description = "Creates a resource (ADMIN only)",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse> createResource(@Valid @RequestBody CreateResourceDTO dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(ApiResponse.success("Resource created",
                resourceService.createResource(dto, username)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateResource(@PathVariable UUID id, @Valid @RequestBody UpdateResourceDTO dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(ApiResponse.success("Resource updated",
                resourceService.updateResource(id, dto, username)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteResource(@PathVariable UUID id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        resourceService.deleteResource(id, username);
        return ResponseEntity.ok(ApiResponse.success("Resource deleted", null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getResource(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success("Resource found",
                resourceService.getResourceById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllResources(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return ResponseEntity.ok(ApiResponse.success("Resources fetched",
                resourceService.getAllResources(pageable)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchResources(
            @RequestParam String searchKey,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return ResponseEntity.ok(ApiResponse.success("Resources found",
                resourceService.searchResources(searchKey, pageable)));
    }
}