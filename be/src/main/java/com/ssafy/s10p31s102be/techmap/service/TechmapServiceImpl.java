package com.ssafy.s10p31s102be.techmap.service;

import com.ssafy.s10p31s102be.common.exception.InvalidAuthorizationException;
import com.ssafy.s10p31s102be.common.exception.KeywordNotFoundException;
import com.ssafy.s10p31s102be.common.infra.entity.Keyword;
import com.ssafy.s10p31s102be.common.infra.enums.KeywordType;
import com.ssafy.s10p31s102be.common.infra.repository.KeywordJpaRepository;
import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.member.exception.DepartmentNotFoundException;
import com.ssafy.s10p31s102be.member.exception.MemberNotFoundException;
import com.ssafy.s10p31s102be.member.infra.entity.Department;
import com.ssafy.s10p31s102be.member.infra.entity.Member;
import com.ssafy.s10p31s102be.member.infra.repository.DepartmentJpaRepository;
import com.ssafy.s10p31s102be.member.infra.repository.MemberJpaRepository;
import com.ssafy.s10p31s102be.techmap.dto.request.TechmapCreateDto;
import com.ssafy.s10p31s102be.techmap.dto.request.TechmapFindDto;
import com.ssafy.s10p31s102be.techmap.dto.request.TechmapUpdateDto;
import com.ssafy.s10p31s102be.techmap.dto.response.TechmapPageDto;
import com.ssafy.s10p31s102be.techmap.dto.response.TechmapReadDto;
import com.ssafy.s10p31s102be.techmap.dto.response.TargetYearReadDto;
import com.ssafy.s10p31s102be.techmap.exception.TechmapNotFoundException;
import com.ssafy.s10p31s102be.techmap.infra.entity.Techmap;
import com.ssafy.s10p31s102be.techmap.infra.entity.TechmapDepartment;
import com.ssafy.s10p31s102be.techmap.infra.entity.TechmapKeyword;
import com.ssafy.s10p31s102be.techmap.infra.repository.TechmapJpaRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TechmapServiceImpl implements TechmapService {
    private final TechmapJpaRepository techmapRepository;
    private final MemberJpaRepository memberRepository;
    private final DepartmentJpaRepository departmentRepository;
    private final KeywordJpaRepository keywordRepository;

    private final KeywordType KEYWORD_IDENTIFIER = KeywordType.techmap;

    /*
        관리자와 운영진만이 techmap를 생성할 수 있다.
     */
    @Override
    public Techmap create(UserDetailsImpl userDetails, TechmapCreateDto techmapCreateDto) {
        // 관리자(AuthorityLevel 1) & 운영진(AuthorityLevel 2)를 제외한 인원은 인재Pool을 등록할 수 없다.
        if (userDetails.getAuthorityLevel() > 2) {
            throw new InvalidAuthorizationException(userDetails.getMemberId(), this);
        }

        // 관리자와 운영진이라면 create 가능
        Integer memberId = userDetails.getMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId, this));

        Techmap techmap = Techmap.builder()
                .member(member)
                .description(techmapCreateDto.getDescription())
                .dueDate(techmapCreateDto.getDueDate())
                .isAlarmSend(techmapCreateDto.getIsAlarmSend())
                .targetYear(techmapCreateDto.getTargetYear())
                .build();

        // 입력 받은 부서들의 정보
        List<Integer> departments = techmapCreateDto.getDepartments();
        for (Integer departmentId : departments) {
            Department department = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new DepartmentNotFoundException(departmentId, this));

            TechmapDepartment techmapDepartment = new TechmapDepartment(techmap, department);
            techmap.getTechmapDepartments().add(techmapDepartment);
        }

        /*
            키워드 정보
            만약 키워드가 존재하지 않는다면, 새롭게 키워드를 추가
            키워드가 존재한다면, 기존 키워드 활용 횟수를 카운팅
         */
        List<String> keywords = techmapCreateDto.getKeywords();

        for (String word : keywords) {
            Keyword keyword = keywordRepository.findByTypeAndData(KEYWORD_IDENTIFIER, word)
                    .orElseGet(() -> new Keyword(KEYWORD_IDENTIFIER, word));
            keyword.use();

            TechmapKeyword techmapKeyword = new TechmapKeyword(techmap, keyword);
            techmap.getTechmapKeywords().add(techmapKeyword);
        }

        techmapRepository.save(techmap);

        return techmap;
    }

    /*
        운영진과 관리자는 모든 인재Pool을 조회가 가능하며 각 부서별로 내려준 인재Pool을 체크가 가능하다.
        각 부서의 채용부서장과 채용담당자들은 자신이 속한 부서의 인재Pool만을 조회가 가능하다.
     */
    @Override
    public TechmapPageDto findtechmaps(UserDetailsImpl userDetails, TechmapFindDto techmapFindDto) {
        Pageable pageable = PageRequest.of(techmapFindDto.getPageNumber(), techmapFindDto.getSize(), Sort.by(Sort.Direction.DESC, "targetYear"));
        Integer targetYear = techmapFindDto.getTargetYear();
        Integer departmentId = techmapFindDto.getDepartmentId();

        // 부서 별로 조회를 원한다면 관리자와 운영진만 가능하다.
        if (departmentId != null && userDetails.getAuthorityLevel() > 2) {
            throw new InvalidAuthorizationException(userDetails.getMemberId(), this);
        }

        // 채용담당자와 채용부서장들은 자신이 속한 부서의 인재Pool만을 확인 가능하다.
        if (userDetails.getAuthorityLevel() > 2) {
            departmentId = userDetails.getDepartmentId();
        }

        Page<Techmap> pages = techmapRepository.findtechmaps(pageable, targetYear, departmentId);
        List<TechmapReadDto> techmaps = new ArrayList<>();
        if (departmentId != null) {
            Integer finalDepartmentId = departmentId;
            techmaps = pages.getContent().stream()
                    .map(r -> TechmapReadDto.fromEntity(r, finalDepartmentId)).toList();
        } else{
            techmaps = pages.getContent().stream()
                    .map(TechmapReadDto::fromEntity).toList();
        }

        return new TechmapPageDto(techmaps, searchTargetYear(departmentId), pages.getTotalPages(), pages.getTotalElements(), pages.getNumber(), pages.getSize());

    }

    private List<TargetYearReadDto> searchTargetYear(Integer departmentId) {
        List<TargetYearReadDto> list = new ArrayList<>();

        List<Object[]> results = techmapRepository.findTargetYear(departmentId);
        for (Object[] result : results) {
            Integer targetYear = (Integer) result[0];
            Long count = (Long) result[1];

            list.add(new TargetYearReadDto(targetYear, count));
        }

        return list;
    }

    /*
        관리자와 운영진만이 techmap를 수정할 수 있다.
     */
    @Override
    public Techmap update(UserDetailsImpl userDetails, Integer techmapId, TechmapUpdateDto techmapUpdateDto) {
        // 관리자(AuthorityLevel 1) & 운영진(AuthorityLevel 2)를 제외한 인원은 인재Pool을 수정할 수 없다.
        if (userDetails.getAuthorityLevel() > 2) {
            throw new InvalidAuthorizationException(userDetails.getMemberId(), this);
        }

        Techmap techmap = techmapRepository.findById(techmapId)
                .orElseThrow(() -> new TechmapNotFoundException(techmapId, this));

        // department 리스트 초기화
        techmap.getTechmapDepartments().clear();

        // techmap count 횟수 초기화 이후 리스트 초기화
        List<TechmapKeyword> TechmapKeywords = techmap.getTechmapKeywords();
        for (TechmapKeyword techmapKeyword : TechmapKeywords) {
            Long keywordId = techmapKeyword.getKeyword().getId();
            Keyword keyword = keywordRepository.findById(keywordId)
                    .orElseThrow(() -> new KeywordNotFoundException(keywordId, this));
            keyword.drop();
        }
        techmap.getTechmapKeywords().clear();

        techmap.updatetechmap(techmapUpdateDto.getDueDate(),
                techmapUpdateDto.getTargetYear(),
                techmapUpdateDto.getDescription());

        // 부서 재배정
        List<Integer> departments = techmapUpdateDto.getDepartments();
        for (Integer departmentId : departments) {
            Department department = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new DepartmentNotFoundException(departmentId, this));

            techmap.getTechmapDepartments().add(new TechmapDepartment(techmap, department));
        }

        /*
            키워드 정보
            만약 키워드가 존재하지 않는다면, 새롭게 키워드를 추가
            키워드가 존재한다면, 기존 키워드 활용 횟수를 카운팅
         */
        List<String> keywords = techmapUpdateDto.getKeywords();

        for (String word : keywords) {
            Keyword keyword = keywordRepository.findByTypeAndData(KEYWORD_IDENTIFIER, word)
                    .orElseGet(() -> new Keyword(KEYWORD_IDENTIFIER, word));

            techmap.getTechmapKeywords().add(new TechmapKeyword(techmap, keyword));
        }

        return techmap;
    }

    /*
        관리자와 운영자가 자신이 만든 인재Pool 프로젝트를 삭제 가능
     */
    @Override
    public void delete(UserDetailsImpl userDetails, List<Integer> techmapIds) {
        if (userDetails.getAuthorityLevel() > 2) {
            throw new InvalidAuthorizationException(userDetails.getMemberId(), this);
        }

        for (Integer techmapId : techmapIds) {
            techmapRepository.deleteById(techmapId);
        }
    }
}