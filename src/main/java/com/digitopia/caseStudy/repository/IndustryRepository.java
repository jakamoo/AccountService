package com.digitopia.caseStudy.repository;

import com.digitopia.caseStudy.dto.IndustryDto;
import com.digitopia.caseStudy.entity.IndustryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IndustryRepository extends JpaRepository<IndustryEntity,Long> {

    Optional<IndustryEntity> findById(Long organizationId);


}
