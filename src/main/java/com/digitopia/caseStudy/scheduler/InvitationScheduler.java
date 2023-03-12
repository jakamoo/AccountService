package com.digitopia.caseStudy.scheduler;

import com.digitopia.caseStudy.entity.InvitationEntity;
import com.digitopia.caseStudy.repository.InvitationRepository;
import com.digitopia.caseStudy.status.InvitationStatus;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@AllArgsConstructor
public class InvitationScheduler {


    private InvitationRepository invitationRepository;

    @Scheduled(cron = "* * * * * *")
    public void taskExpirationScheduler() {
        LocalDate now = LocalDate.now();
        List<InvitationEntity> invitations=invitationRepository.findAll();


        for (InvitationEntity invitation : invitations) {
            if (isExpired(invitation)
                    && invitation.getStatus()==InvitationStatus.PENDING) {
                invitation.setStatus(InvitationStatus.EXPIRED);
                invitation.setInvitationCode("-");

                invitationRepository.save(invitation);

                System.out.println(invitation.getUserId());
            }
        }
    }




    private boolean isExpired(InvitationEntity invitation) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(invitation.getCreatedDate());
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        Date oneWeekLater = cal.getTime();
        Date now = new Date();
        return now.after(oneWeekLater);
    }
}


