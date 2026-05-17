package com.airbnb.propertyservice.service;

import com.airbnb.propertyservice.dto.Propertydto;
import com.airbnb.propertyservice.entity.Property;
import com.airbnb.propertyservice.exception.ResourceNotFoundException;
import com.airbnb.propertyservice.repository.PropertyRepository;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.airbnb.propertyservice.service.serviceMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class PropertyService
{

    private final PropertyRepository repo;

    public serviceMethod serviceMethod;

    public PropertyService(PropertyRepository repo ,serviceMethod serviceMethod) {
        this.repo = repo;
        this.serviceMethod = serviceMethod;
    }


    public Propertydto getById(Long id)
    {
        System.out.println("I am here");
        Property property1= repo.findById(id).orElseThrow(() ->
            new ResourceNotFoundException("Property not found with id: " + id));;

        return serviceMethod.mapToDto(property1);
    }

    // CREATE PROPERTY
    public Propertydto create(Propertydto dto) {

        Property property = serviceMethod.mapToEntity(dto);

        property.setAvailable(true);

        Property saved = repo.save(property);

        return serviceMethod.mapToDto(saved);
    }

    // GET ALL
    public List<Propertydto> getAll() {

        return repo.findAll()
                .stream()
                .map(serviceMethod::mapToDto)
                .toList();
    }

    // SEARCH
    public Page<Propertydto> search(String location , Pageable pageable) {

        return repo.findByLocationContainingIgnoreCase(location ,pageable)
                .map(serviceMethod::mapToDto);
    }

    public boolean delete(Long id)
    {
        Property property = repo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Property not found"));
        if(property!=null) {
            repo.deleteById(id);
            return true;
        }
        else {
            return false;

    }

    }
    public Propertydto update(Long id, Propertydto dto) {

        Property property = repo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Property not found"));

        property.setTitle(dto.getTitle());
        property.setLocation(dto.getLocation());
        property.setPricePerNight(dto.getPricePerNight());
        property.setTotalGuests(dto.getTotalGuests());
        property.setAvailable(dto.getAvailable());
        property.setOwnerName(dto.getOwnerName());

        Property updated = repo.save(property);

        return serviceMethod.mapToDto(updated);
    }

}
