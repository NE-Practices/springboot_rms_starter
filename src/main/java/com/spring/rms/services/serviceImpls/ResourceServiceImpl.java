package com.spring.rms.services.serviceImpls;

import com.spring.rms.models.Resource;
import com.spring.rms.payload.request.CreateResourceDTO;
import com.spring.rms.payload.request.UpdateResourceDTO;
import com.spring.rms.repositories.IResourceRepository;
import com.spring.rms.services.IMailService;
import com.spring.rms.services.IResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements IResourceService {

    private final IResourceRepository resourceRepository;
    private final IMailService mailService;

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

    @Override
    public Page<Resource> getAllResources(Pageable pageable) {
        return resourceRepository.findAll(pageable);
    }

    @Override
    public Page<Resource> searchResources(String searchKey, Pageable pageable) {
        return resourceRepository.findByNameContainingIgnoreCase(searchKey, pageable);
    }
}