package com.digitopia.caseStudy.controller;

import com.digitopia.caseStudy.dto.InvitationDto;
import com.digitopia.caseStudy.service.InvitationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/invitations")
public class InvitationController {
    private final InvitationService invitationService;

    @GetMapping
    public ResponseEntity<List<InvitationDto>> getAll() {
        List<InvitationDto> invitations = invitationService.getInvitations();

        if(invitations.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(invitations,HttpStatus.OK);
    }



    @PutMapping("/{oldInvitationId}")
    public ResponseEntity<Void> updateInvitation(@PathVariable Long oldInvitationId,
                                                 @RequestBody InvitationDto invitationDto,
                                                 @RequestParam Long updaterId) {

        invitationService.updateInvitation(updaterId, invitationDto,oldInvitationId);


        return new ResponseEntity<>(HttpStatus.OK);


    }



    @PostMapping("/{creatorId}")
    public ResponseEntity<InvitationDto> createInvitation(@RequestBody InvitationDto invitationDto,@PathVariable Long creatorId) {

        invitationService.createInvitation(invitationDto,creatorId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/accept/{invitationCode}")
    public ResponseEntity<InvitationDto> acceptInvitation(@PathVariable String invitationCode) {

        invitationService.acceptInvitation(invitationCode);
        return new ResponseEntity<>(HttpStatus.OK);
    }





    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvitation(@PathVariable Long id) {
        if (invitationService.deleteInvitation(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{userId}/invitations")
    public ResponseEntity<List<InvitationDto>> getInvitationsByUser(@PathVariable Long userId) {

        List<InvitationDto> invitations = invitationService.getInvitationsByUser(userId);

        if(invitations.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(invitations,HttpStatus.OK);
    }






}
