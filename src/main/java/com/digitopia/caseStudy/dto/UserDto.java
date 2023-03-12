package com.digitopia.caseStudy.dto;

import com.digitopia.caseStudy.status.UserStatus;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserDto {


    private String fullName;
    private String email;

    private List<Long> industryIds;

}



