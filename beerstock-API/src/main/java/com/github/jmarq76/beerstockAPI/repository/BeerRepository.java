package com.github.jmarq76.beerstockAPI.repository;

import com.github.jmarq76.beerstockAPI.entity.Beer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BeerRepository extends JpaRepository<Beer, Long> {

    Optional<Beer> findByName(String name);
}
