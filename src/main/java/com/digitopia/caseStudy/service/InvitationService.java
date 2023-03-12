package com.digitopia.caseStudy.service;

import com.digitopia.caseStudy.dto.IndustryDto;
import com.digitopia.caseStudy.dto.InvitationDto;
import com.digitopia.caseStudy.entity.IndustryEntity;
import com.digitopia.caseStudy.entity.InvitationEntity;
import com.digitopia.caseStudy.entity.UserEntity;
import com.digitopia.caseStudy.exception.InvitationExpiredException;
import com.digitopia.caseStudy.exception.InvitationNotFoundException;
import com.digitopia.caseStudy.repository.IndustryRepository;
import com.digitopia.caseStudy.repository.InvitationRepository;
import com.digitopia.caseStudy.repository.UserRepository;
import com.digitopia.caseStudy.status.InvitationStatus;
import com.sun.jdi.InternalException;
import lombok.AllArgsConstructor;

import org.modelmapper.ModelMapper;

import org.springframework.stereotype.Service;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InvitationService {

    private final InvitationRepository invitationRepository;

    private final ModelMapper modelMapper;


    private final UserRepository userRepository;

    private final MailService mailService;





    public List<InvitationDto> getInvitations(){

        List<InvitationEntity> invitationEntities = (List<InvitationEntity>) invitationRepository.findAll();

        return invitationEntities.stream()
                .map(invitationEntity -> modelMapper.map(invitationEntity, InvitationDto.class))
                .collect(Collectors.toList());

    }



    public List<InvitationDto> getInvitationsByUser(Long userId) {

        List<InvitationEntity> invitationEntities = invitationRepository.findByUserId(userId);


        if(!invitationEntities.isEmpty()){

            return invitationEntities.stream()
                    .map(invitationEntity -> modelMapper.map(invitationEntity, InvitationDto.class))
                    .collect(Collectors.toList());
        }


        throw new RuntimeException("There is no any Invitation");
    }







    public void createInvitation(InvitationDto invitationDto, Long creatorId) {
            InvitationEntity invitationEntity = modelMapper.map(invitationDto, InvitationEntity.class);
            Long userId =invitationEntity.getUserId();
            InvitationStatus pending = InvitationStatus.PENDING;

            if (invitationRepository.findByUserIdAndStatus(userId,pending).isPresent()) {
                throw new RuntimeException("A user can only have one pending invitation.");
            }

            invitationEntity.setCreatedBy(creatorId);
            invitationEntity.setCreatedDate(new Date());
            invitationEntity.setUpdatedBy(0L);
            invitationEntity.setUpdatedDate(null);
            invitationEntity.setStatus(InvitationStatus.PENDING);
            invitationEntity.setInvitationCode(invitationCodeGenerator(invitationEntity));

            sendMail(invitationDto);


            invitationRepository.save(invitationEntity);



    }

    private void sendMail(InvitationDto invitationDto){ String userEmail= userRepository.findById(invitationDto.getUserId()).get().getEmail();
        String userName = userRepository.findById(invitationDto.getUserId()).get().getFullName();
        String subject = "From: "+userName;
        mailService.sendEmail(userEmail,subject,invitationDto.getMessage());
    }


    private String invitationCodeGenerator(InvitationEntity invitationEntity){
        Long id = invitationEntity.getUserId();
        Optional <UserEntity> user = userRepository.findById(id);
        String mail = user.get().getEmail();
        String date = user.get().getCreatedDate().toString();

        return invitationCodeGeneratorHelper(mail,date);

    }

    private String invitationCodeGeneratorHelper(String email, String date) {

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            String invitationString = email + "_" + date;

            byte[] invitationBytes = invitationString.getBytes();

            byte[] digestBytes = md.digest(invitationBytes);


            StringBuilder sb = new StringBuilder();
            for (byte b : digestBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not available", e);
        }
    }



    private String normalizeName(String name) {
        return name.toLowerCase().replaceAll("[^a-z ]+", "");
    }


    public boolean deleteInvitation(Long id) {
        try {
            invitationRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public void updateInvitation(Long creatorId, InvitationDto newInvitation, Long olInvitationId) {

        Optional<InvitationEntity> invitation = invitationRepository.findById(olInvitationId);



        if (invitation.isEmpty()){
            throw new RuntimeException("Invitation Not Found");
        }

        else {

            InvitationEntity oldInvitation = invitation.get();
            oldInvitation.setUpdatedDate(new Date());
            oldInvitation.setUpdatedBy(creatorId);
            invitationRepository.save(oldInvitation);
        }
    }

    public void acceptInvitation(String invitationCode) {

       Optional<InvitationEntity> invitation = invitationRepository.findByInvitationCode(invitationCode);

        if(invitation.isPresent()){

            if(invitation.get().getStatus()==InvitationStatus.EXPIRED){
                throw new InvitationExpiredException("Invitation is Expired ");
            }

           else{

               invitation.get().setStatus(InvitationStatus.ACCEPTED);
               invitationRepository.save(invitation.get());
            }


        }

        else {
            throw new InvitationNotFoundException("Invitation is NotFound");

        }
    }
}

