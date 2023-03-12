package com.digitopia.caseStudy.repository;

import com.digitopia.caseStudy.entity.UserEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    @Transactional
    @Modifying
    @Query("update UserEntity u set u.createdDate = ?1, u.updatedDate = ?2")
    void updateCreatedDateAndUpdatedDateBy(Date createdDate, Date updatedDate);

    Optional<UserEntity> findById(Long id);
    Optional<UserEntity>  findByEmail(String email);
    List<UserEntity>findAll();
    List<UserEntity> findAllById(Iterable<Long> id);

    List<UserEntity> findAllByNormalizedName(String normalizedName);
}
