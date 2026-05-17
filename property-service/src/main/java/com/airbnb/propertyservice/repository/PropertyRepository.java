package com.airbnb.propertyservice.repository;

import com.airbnb.propertyservice.entity.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PropertyRepository extends JpaRepository<Property , Long>
{
    Page<Property> findByLocationContainingIgnoreCase(
            String location,
            Pageable pageable
    );


}
