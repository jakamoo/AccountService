package com.digitopia.caseStudy.service;

import com.digitopia.caseStudy.dto.IndustryDto;
import com.digitopia.caseStudy.dto.UserDto;
import com.digitopia.caseStudy.entity.IndustryEntity;
import com.digitopia.caseStudy.entity.UserEntity;
import com.digitopia.caseStudy.map.IndustryMapper;
import com.digitopia.caseStudy.map.UserMapper;
import com.digitopia.caseStudy.repository.IndustryRepository;
import com.digitopia.caseStudy.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;




@Service
@AllArgsConstructor
public class IndustryService {

    private final UserRepository userRepository;

    private final IndustryRepository industryRepository;
    private final UserMapper userMapper;
    private final IndustryMapper industryMapper;


    public List<IndustryDto> getIndustries() {

        List<IndustryEntity> industryEntities =  industryRepository.findAll();

        return industryMapper.convertToDto(industryEntities);
    }

    public void updateIndustry(Long creatorId, IndustryDto industryDto, Long oldIndustryId) {

        Optional<IndustryEntity> industry = industryRepository.findById(oldIndustryId);


        if (industry.isEmpty()){
            throw new RuntimeException("Industry Not Found");
        }

        else {

            IndustryEntity oldIndustry = industry.get();
            oldIndustry.setUpdatedDate(new Date());
            oldIndustry.setUpdatedBy(creatorId);

            if(!userValidator(industryDto).isEmpty())

                throw new IllegalArgumentException("User IDs not found: " + userValidator(industryDto));

            industryRepository.save(oldIndustry);
        }
    }



    public void createIndustry(IndustryDto industryDto, Long creatorId) {

            IndustryEntity industryEntity = industryMapper.convertToEntity(industryDto);
            industryEntity.setCreatedBy(creatorId);
            industryEntity.setCreatedDate(new Date());
            industryEntity.setUpdatedBy(0L);
            industryEntity.setUpdatedDate(null);
            industryEntity.setNormalizedName(normalizeNameIndustry(industryEntity.getName()));

            if(!userValidator(industryDto).isEmpty())

                throw new IllegalArgumentException("User IDs not found: " + userValidator(industryDto));


        industryRepository.save(industryEntity);




    }


    private List<Long> userValidator(IndustryDto industryDto){

        List<Long> userIds=industryDto.getUserIds();
        List<UserEntity> userEntities = userRepository.findAllById(userIds);

        List<Long> foundIds = userEntities.stream()
                .map(UserEntity::getId)
                .collect(Collectors.toList());

        List<Long> notFoundIds = userIds.stream()
                .filter(id -> !foundIds.contains(id))
                .collect(Collectors.toList());


        return notFoundIds;

    }



    private String normalizeNameIndustry(String name) {

         return name.toLowerCase().replaceAll("[^a-z0-9 ]+", "");
    }

    public boolean deleteIndustry(Long id) {
        return false;
    }


    public List<UserDto> getAllUsers(Long id){

        Optional<IndustryEntity> industryEntity = industryRepository.findById(id);


        if(industryEntity.isPresent()){
            List<UserEntity> userEntities = industryEntity.get().getUsers();

            return userMapper.convertToDto(userEntities);
        }



        else
            return null;


    }

    public List<IndustryDto> getAllSubIndustry(Long industryId) {

        Optional<IndustryEntity> industryEntity = industryRepository.findById(industryId);
    return null;
    }
}
