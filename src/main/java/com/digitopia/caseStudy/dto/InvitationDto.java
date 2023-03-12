package com.digitopia.caseStudy.dto;

import com.digitopia.caseStudy.status.InvitationStatus;

import lombok.Data;

@Data
public class InvitationDto {
    private Long userId;
    private String message;

    private InvitationStatus status;
}
