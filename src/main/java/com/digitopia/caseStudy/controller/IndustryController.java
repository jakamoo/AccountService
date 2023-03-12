package com.digitopia.caseStudy.controller;

import com.digitopia.caseStudy.dto.IndustryDto;
import com.digitopia.caseStudy.dto.UserDto;
import com.digitopia.caseStudy.service.IndustryService;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/industries")

public class IndustryController {

    private final IndustryService industryService;


    @GetMapping
    public ResponseEntity<List<IndustryDto>> getAll() {
        List<IndustryDto> industries = industryService.getIndustries();

        if(industries.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(industries,HttpStatus.OK);
    }


    @PutMapping("/{oldIndustryId}")
    public ResponseEntity<Void> updateIndustry(@PathVariable Long oldIndustryId,
                                               @RequestBody IndustryDto industryDto,
                                               @RequestParam Long creatorId) {

        industryService.updateIndustry(creatorId, industryDto,oldIndustryId);


        return new ResponseEntity<>(HttpStatus.OK);


    }



    @PostMapping("/{creatorId}")
    public ResponseEntity<IndustryDto> createIndustry(@RequestBody IndustryDto industryDto,@PathVariable Long creatorId) {

        industryService.createIndustry(industryDto,creatorId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIndustry(@PathVariable Long id) {
        if (industryService.deleteIndustry(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @GetMapping("/{industry_id}/users")
    public ResponseEntity<List<UserDto>> getAllUsers(@PathVariable Long industry_id) {

        List<UserDto> users = industryService.getAllUsers(industry_id);

        if(users.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(users,HttpStatus.OK);


    }

    @GetMapping("/{industry_id}/all")
    public ResponseEntity<List<IndustryDto>> getAllSubIndustry(@PathVariable Long industry_id) {

        List<IndustryDto> industries = industryService.getAllSubIndustry(industry_id);

        if(industries.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(industries,HttpStatus.OK);


    }


}


