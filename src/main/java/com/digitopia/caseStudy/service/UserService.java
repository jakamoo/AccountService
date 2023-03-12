package com.digitopia.caseStudy.service;

import com.digitopia.caseStudy.dto.IndustryDto;
import com.digitopia.caseStudy.dto.UserDto;
import com.digitopia.caseStudy.entity.IndustryEntity;
import com.digitopia.caseStudy.entity.UserEntity;
import com.digitopia.caseStudy.exception.UserSameMailAdressExistException;
import com.digitopia.caseStudy.map.IndustryMapper;
import com.digitopia.caseStudy.map.UserMapper;
import com.digitopia.caseStudy.repository.IndustryRepository;
import com.digitopia.caseStudy.repository.UserRepository;
import com.digitopia.caseStudy.status.UserStatus;
import com.sun.jdi.InternalException;
import lombok.AllArgsConstructor;


import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final IndustryMapper industryMapper;


    private final IndustryRepository industryRepository;

    private final UserMapper userMapper;






    public List<UserDto> getUsers(){

        List<UserEntity> userEntities =  userRepository.findAll();

       return userMapper.convertToDto(userEntities);


    }

    public List<IndustryDto> getIndustries(Long id){

        Optional<UserEntity> userEntity = userRepository.findById(id);

        if(userEntity.isPresent()){

            return industryMapper.convertToDto(userEntity.get().getIndustries());
        }

        else
            return null;


    }


    public void createUser(UserDto userDto,Long creatorId) {

        try {
            UserEntity userEntity = userMapper.convertToEntity(userDto);
            userEntity.setCreatedBy(creatorId);
            userEntity.setCreatedDate(new Date());
            userEntity.setUpdatedBy(0L);
            userEntity.setUpdatedDate(null);
            userEntity.setNormalizedName(normalizeName(userEntity.getFullName()));
            userEntity.setStatus(UserStatus.ACTIVE);

            if(!industryValidator(userDto).isEmpty()){
                throw new IllegalArgumentException("Invitation IDs not found: " + industryValidator(userDto));

            }



            userRepository.save(userEntity);

        } catch (IllegalArgumentException e){
            throw new RuntimeException(e.getMessage());

        }catch (RuntimeException e) {

            throw new RuntimeException(e.getMessage());
        }

        catch (Exception e) {

            throw new UserSameMailAdressExistException("There is user already defined with same email.");
        }

    }


    public List<Long> industryValidator(UserDto userDto){

        List<Long> industryIds=userDto.getIndustryIds();
        List<IndustryEntity> industryEntities = industryRepository.findAllById(industryIds);


        List<Long> foundIds = industryEntities.stream()
                .map(IndustryEntity::getId)
                .collect(Collectors.toList());

        List<Long> notFoundIds = industryIds.stream()
                .filter(id -> !foundIds.contains(id))
                .collect(Collectors.toList());


        return notFoundIds;

    }

    private String normalizeName(String name) {

        return name.toLowerCase().replaceAll("[^a-z ]+", "");
    }


    public boolean deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public List<UserDto> findUsersByNormalizedName(String normalizedName) {
        List<UserEntity> userEntities = userRepository.findAllByNormalizedName(normalizedName);
        return userMapper.convertToDto(userEntities);
    }

    public  UserDto getUserByEmail(String email) {
        Optional<UserEntity> userEntity = userRepository.findByEmail(email);


        if(userEntity.isPresent()){
            return  userMapper.convertToDto(userEntity.get());
        }

        else
            throw new RuntimeException("User Not Found");
    }




    public void updateUser(Long creatorId, UserDto newUser, Long olUserId) {
        Optional<UserEntity> user = userRepository.findById(olUserId);


        if (user.isEmpty()){
            throw new RuntimeException("User Not Found");
        }

        else {

            UserEntity oldUser = user.get();
            oldUser.setFullName(newUser.getFullName());
            oldUser.setNormalizedName(normalizeName(newUser.getFullName()));
            oldUser.setEmail(newUser.getEmail());
            oldUser.setUpdatedDate(new Date());
            oldUser.setUpdatedBy(creatorId);

            if(!industryValidator(newUser).isEmpty())

                throw new IllegalArgumentException("Following Industry ids not found: " + industryValidator(newUser));

            userRepository.save(oldUser);




        }
    }
}

