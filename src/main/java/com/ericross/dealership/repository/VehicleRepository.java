package com.ericross.dealership.repository;

import com.ericross.dealership.entity.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<CarEntity, Long> {
    // Custom query methods (optional)
    List<CarEntity> findByColor(String color);
    List<CarEntity> findByModel(String model);
    List<CarEntity> findAll();
}
