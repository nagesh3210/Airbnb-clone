package com.airbnb.propertyservice.controller;

import com.airbnb.propertyservice.dto.Propertydto;
import com.airbnb.propertyservice.entity.Property;
import com.airbnb.propertyservice.service.PropertyService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/property")
public class PropertyController {
    private final PropertyService service;

    public PropertyController(PropertyService service) {
        this.service = service;
    }


    // CREATE PROPERTY
    @PostMapping
    public ResponseEntity<Propertydto> createProperty(
            @Valid @RequestBody Propertydto propertyDto) {

        Propertydto savedProperty =
                service.create(propertyDto);

        return new ResponseEntity<>(
                savedProperty,
                HttpStatus.CREATED
        );
    }

    // GET ALL PROPERTIES
    @GetMapping
    public ResponseEntity<List<Propertydto>> getAllProperties() {

        List<Propertydto> properties =
                service.getAll();

        return ResponseEntity.ok(properties);
    }

    // GET PROPERTY BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Propertydto> getPropertyById(
            @PathVariable Long id) {

        Propertydto property = service.getById(id);
        return ResponseEntity.ok(property);
    }
     //GET /property/search?location=goa&page=0&size=5
    // SEARCH PROPERTIES
    //GET /property/search?location=goa&sort=pricePerNight,desc
    @GetMapping("/search")
    public ResponseEntity<Page<Propertydto>> searchProperties(
            @RequestParam String location ,
            @PageableDefault(
                    size = 5,
                    sort = "pricePerNight"
            ) Pageable pageable) {

        Page<Propertydto> search = service.search(location, pageable);

        return ResponseEntity.ok(search);
    }

    // DELETE PROPERTY
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProperty(
            @PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.ok(
                "Property deleted successfully"
        );
    }

    // UPDATE PROPERTY
//    @PutMapping("/{id}")
//    public ResponseEntity<Propertydto> updateProperty(
//            @PathVariable Long id,
//            @Valid @RequestBody Propertydto propertyDto) {
//
//        PropertyDto updatedProperty =
//                service.update(id, propertyDto);
//
//        return ResponseEntity.ok(updatedProperty);
//    }
}