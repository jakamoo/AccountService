package com.digitopia.caseStudy.dto;

import lombok.Data;

import java.util.List;

@Data
public class IndustryDto {
    private String name;

    private List<Long> parentIndustries;
    private List<Long> userIds;
}
