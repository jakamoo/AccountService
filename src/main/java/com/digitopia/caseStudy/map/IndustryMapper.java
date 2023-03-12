package com.digitopia.caseStudy.map;

import com.digitopia.caseStudy.dto.IndustryDto;
import com.digitopia.caseStudy.entity.UserEntity;
import com.digitopia.caseStudy.entity.IndustryEntity;
import com.digitopia.caseStudy.repository.UserRepository;
import com.digitopia.caseStudy.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class IndustryMapper {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public IndustryMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
        modelMapper = new ModelMapper();
    }

    public IndustryDto convertToDto(IndustryEntity industryEntity) {
        IndustryDto industryDto = modelMapper.map(industryEntity, IndustryDto.class);
        if (industryEntity.getUsers() != null) {
            industryDto.setUserIds(industryEntity.getUsers().stream().map(UserEntity::getId).collect(Collectors.toList()));
        }
        return industryDto;
    }

    public IndustryEntity convertToEntity(IndustryDto industryDto ) {

        try {

            IndustryEntity industryEntity = modelMapper.map(industryDto, IndustryEntity.class);
            if (industryDto.getUserIds() != null) {
                List<UserEntity> users = industryDto.getUserIds().stream().map(id -> {
                    return userRepository.findById(id).get();
                }).collect(Collectors.toList());
                industryEntity.setUsers(users);
            }

            return industryEntity;

        }
        catch (Exception e){
            throw new RuntimeException("Bad inputs");
        }

    }
    public List<IndustryDto> convertToDto(List<IndustryEntity> industryEntityList) {
        try {

            return industryEntityList.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());

        }

         catch (Exception e){
            throw new RuntimeException("Bad inputs");
        }
    }

    public List<IndustryEntity> convertToEntity(List<IndustryDto> industryDtoList) {
        try {
        return industryDtoList.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());
        }
           catch (Exception e){
                throw new RuntimeException("Bad inputs");
            }
    }
}