package com.digitopia.caseStudy.entity;

import com.digitopia.caseStudy.status.InvitationStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;


@Table(name="INVITATION_TABLE")
@Entity
@Data
public class InvitationEntity extends BaseEntity {

    @Column(name = "user_id")
    private Long userId;
    @Column(name = "message")
    private String message;
    @Column(name="invitation_code",unique = true)
    private  String invitationCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "invitation_status")
    private InvitationStatus status;


}
