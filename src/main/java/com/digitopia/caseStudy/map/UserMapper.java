package com.digitopia.caseStudy.map;

import com.digitopia.caseStudy.dto.UserDto;
import com.digitopia.caseStudy.entity.IndustryEntity;
import com.digitopia.caseStudy.entity.UserEntity;
import com.digitopia.caseStudy.repository.IndustryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class  UserMapper {
    private ModelMapper modelMapper;
    private IndustryRepository industryRepository;

    public UserMapper(IndustryRepository industryRepository) {
        modelMapper = new ModelMapper();
        this.industryRepository=industryRepository;
    }

    public UserDto convertToDto(UserEntity userEntity) {
        try{
        UserDto userDto = modelMapper.map(userEntity, UserDto.class);
        if (userEntity.getIndustries() != null) {
            userDto.setIndustryIds(userEntity.getIndustries().stream().map(IndustryEntity::getId).collect(Collectors.toList()));
        }

        return userDto;

    }

         catch (Exception e){
        throw new RuntimeException("Bad input");
    }

    }

    public UserEntity convertToEntity(UserDto userDto) throws RuntimeException{

        try {
            UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
            if (userDto.getIndustryIds() != null) {
                List<IndustryEntity> industries = userDto.getIndustryIds().stream().map(id -> {
                    return industryRepository.findById(id).get();
                }).collect(Collectors.toList());

                userEntity.setIndustries(industries);
            }
            return userEntity;
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
    public List<UserDto> convertToDto(List<UserEntity> userEntityList) {
        return userEntityList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<UserEntity> convertToEntity(List<UserDto> userDtoList) {

        try {
            return userDtoList.stream()
                    .map(this::convertToEntity)
                    .collect(Collectors.toList());
        }
        catch (Exception e){
            throw new RuntimeException("Bad inputs");
        }
    }
}