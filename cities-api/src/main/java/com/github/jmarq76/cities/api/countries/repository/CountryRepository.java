package com.github.jmarq76.cities.api.countries.repository;

import com.github.jmarq76.cities.api.countries.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {
}
