package com.parkingticket.rateservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.parkingticket.rateservice.entity.RateConfig;

public interface RateRepository extends JpaRepository<RateConfig, Long> {

}
