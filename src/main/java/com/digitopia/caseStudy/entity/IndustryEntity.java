package com.digitopia.caseStudy.entity;

import com.digitopia.caseStudy.dto.UserDto;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;


@Table(name="INDUSTRY_TABLE")
@Entity
@Data

public class IndustryEntity extends BaseEntity {
    private String name;

    private String normalizedName;

    private Long parentIndustryId;
    @ManyToMany(mappedBy = "industries",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<UserEntity> users;




}
