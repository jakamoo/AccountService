package com.digitopia.caseStudy.repository;

import com.digitopia.caseStudy.dto.InvitationDto;
import com.digitopia.caseStudy.entity.IndustryEntity;
import com.digitopia.caseStudy.entity.InvitationEntity;
import com.digitopia.caseStudy.status.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InvitationRepository  extends JpaRepository<InvitationEntity,Long> {
    List<InvitationEntity> findAllById(Long id);

    Optional<InvitationEntity> findByUserIdAndStatus(Long userId, InvitationStatus status);

    List<InvitationEntity> findByUserId(Long userId);


    Optional<InvitationEntity> findByInvitationCode(String invitationCode);
}
