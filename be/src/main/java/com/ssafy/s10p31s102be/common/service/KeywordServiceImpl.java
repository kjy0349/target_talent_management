package com.ssafy.s10p31s102be.common.service;

import com.ssafy.s10p31s102be.common.dto.response.KeywordResponseDto;
import com.ssafy.s10p31s102be.common.dto.response.OptionResponseDto;
import com.ssafy.s10p31s102be.common.infra.entity.Keyword;
import com.ssafy.s10p31s102be.common.infra.enums.KeywordType;
import com.ssafy.s10p31s102be.common.infra.enums.OptionType;
import com.ssafy.s10p31s102be.common.infra.repository.KeywordJpaRepository;
import com.ssafy.s10p31s102be.common.infra.repository.OptionsRepositoryImpl;
import com.ssafy.s10p31s102be.member.infra.entity.Authority;
import com.ssafy.s10p31s102be.member.infra.entity.Department;
import com.ssafy.s10p31s102be.member.infra.entity.Executive;
import com.ssafy.s10p31s102be.member.infra.entity.JobRank;
import com.ssafy.s10p31s102be.member.infra.entity.Member;
import com.ssafy.s10p31s102be.member.infra.entity.Role;
import com.ssafy.s10p31s102be.member.infra.entity.Team;
import com.ssafy.s10p31s102be.member.infra.repository.*;
import com.ssafy.s10p31s102be.networking.infra.repository.ExecutiveJpaRepository;
import com.ssafy.s10p31s102be.profile.infra.entity.Country;
import com.ssafy.s10p31s102be.profile.infra.entity.career.Company;
import com.ssafy.s10p31s102be.profile.infra.entity.education.Lab;
import com.ssafy.s10p31s102be.profile.infra.entity.education.School;
import com.ssafy.s10p31s102be.profile.infra.repository.CompanyJpaRepository;
import com.ssafy.s10p31s102be.profile.infra.repository.CountryJpaRepository;
import com.ssafy.s10p31s102be.profile.infra.repository.LabJpaRepository;
import com.ssafy.s10p31s102be.profile.infra.repository.SchoolJpaRepository;
import com.ssafy.s10p31s102be.techmap.infra.entity.TechMainCategory;
import com.ssafy.s10p31s102be.techmap.infra.repository.TechMainCategoryJpaRepository;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class KeywordServiceImpl implements KeywordService {
    private final OptionsRepositoryImpl optionsRepository;
    private final KeywordJpaRepository keywordRepository;

    @Override
    public List<KeywordResponseDto> readKeywords(KeywordType type, String word) {
        return keywordRepository.findDatasByType(type, 0L, word).stream().map(data -> new KeywordResponseDto(data.getId(), data.getData())).toList();
    }

    @Override
    public List<OptionResponseDto> readOptions(OptionType type, String word) {
        List<OptionResponseDto> optionResponseDtos = new ArrayList<>();

        int limitSize = -1;
        if (word != null){
            limitSize = 5;
        }

        switch (type) {
            case AUTHORITY:
                List<Authority> authorities = optionsRepository.findAuthority(word, limitSize);
                optionResponseDtos = authorities.stream().map(data -> new OptionResponseDto(data.getId().longValue(), data.getAuthName())).toList();
                break;
            case DEPARTMENT:
                List<Department> departments = optionsRepository.findDepartments(word, limitSize);
                optionResponseDtos = departments.stream().map(data -> new OptionResponseDto(data.getId().longValue(), data.getName())).toList();
                break;
            case EXECUTIVE:
                List<Executive> executives = optionsRepository.findExecutives(word, limitSize);
                optionResponseDtos = executives.stream().map(data -> new OptionResponseDto(data.getId().longValue(), data.getName())).toList();
                break;
            case JOBRANK:
                List<JobRank> jobRanks = optionsRepository.findJobRanks(word, limitSize);
                optionResponseDtos = jobRanks.stream().map(data -> new OptionResponseDto(data.getId().longValue(), data.getDescription())).toList();
                break;
            case MEMBER:
                List<Member> members = optionsRepository.findMembers(word, limitSize);
                optionResponseDtos = members.stream().map(data -> new OptionResponseDto(data.getId().longValue(), data.getName())).toList();
                break;
            case ROLE:
                List<Role> roles = optionsRepository.findRoles(word, limitSize);
                optionResponseDtos = roles.stream().map(data -> new OptionResponseDto(data.getId().longValue(), data.getDescription())).toList();
                break;
            case TEAM:
                List<Team> teams = optionsRepository.findTeams(word, limitSize);
                optionResponseDtos = teams.stream().map(data -> new OptionResponseDto(data.getId().longValue(), data.getName())).toList();
                break;
            case COMPANY:
                List<Company> companies = optionsRepository.findCompany(word, limitSize);
                optionResponseDtos = companies.stream().map(data -> new OptionResponseDto(data.getId().longValue(), data.getName())).toList();
                break;
            case LAB:
                List<Lab> labs = optionsRepository.findLabs(word, limitSize);
                optionResponseDtos = labs.stream().map(data -> new OptionResponseDto(data.getLabId().longValue(), data.getLabName())).toList();
                break;
            case SCHOOL:
                List<School> schools = optionsRepository.findSchools(word, limitSize);
                optionResponseDtos = schools.stream().map(data -> new OptionResponseDto(data.getId().longValue(), data.getSchoolName())).toList();
                break;
            case COUNTRY:
                List<Country> countries = optionsRepository.findCountry(word, limitSize);
                optionResponseDtos = countries.stream().map(data -> new OptionResponseDto(data.getId().longValue(), data.getName())).toList();
                break;
            case TECH_MAIN_CATEGORY:
                List<TechMainCategory> techMainCategories = optionsRepository.findTechMainCategory(word, limitSize);
                optionResponseDtos = techMainCategories.stream().map(data -> new OptionResponseDto(data.getId().longValue(), data.getTechMainCategoryName())).toList();
                break;
            default:
                break;
        }

        return optionResponseDtos;
    }
}
