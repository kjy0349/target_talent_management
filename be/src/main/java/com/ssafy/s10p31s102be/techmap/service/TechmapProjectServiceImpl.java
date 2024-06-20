package com.ssafy.s10p31s102be.techmap.service;

import com.ssafy.s10p31s102be.common.exception.InvalidAuthorizationException;
import com.ssafy.s10p31s102be.common.infra.entity.Keyword;
import com.ssafy.s10p31s102be.common.infra.enums.KeywordType;
import com.ssafy.s10p31s102be.common.infra.repository.KeywordJpaRepository;
import com.ssafy.s10p31s102be.common.util.ExcelUtil;
import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.member.exception.MemberNotFoundException;
import com.ssafy.s10p31s102be.member.infra.entity.Member;
import com.ssafy.s10p31s102be.member.infra.repository.MemberJpaRepository;
import com.ssafy.s10p31s102be.profile.dto.request.CompanyCreateDto;
import com.ssafy.s10p31s102be.profile.dto.response.ProfilePreviewDto;
import com.ssafy.s10p31s102be.profile.exception.ProfileNotFoundException;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import com.ssafy.s10p31s102be.profile.infra.entity.career.Company;
import com.ssafy.s10p31s102be.profile.infra.repository.CompanyJpaRepository;
import com.ssafy.s10p31s102be.profile.infra.repository.ProfileJpaRepository;
import com.ssafy.s10p31s102be.profile.service.CompanyService;
import com.ssafy.s10p31s102be.techmap.dto.request.TechmapProjectCreateDto;
import com.ssafy.s10p31s102be.techmap.dto.request.TechmapProjectDuplicateDto;
import com.ssafy.s10p31s102be.techmap.dto.request.TechmapProjectFindDto;
import com.ssafy.s10p31s102be.techmap.dto.request.TechmapProjectProfileFindDto;
import com.ssafy.s10p31s102be.techmap.dto.response.TechmapProjectPageDto;
import com.ssafy.s10p31s102be.techmap.dto.response.TechmapProjectProfilePageDto;
import com.ssafy.s10p31s102be.techmap.dto.response.TechmapProjectReadDto;
import com.ssafy.s10p31s102be.techmap.dto.response.TechmapTargetYearReadDto;
import com.ssafy.s10p31s102be.techmap.exception.TechCompanyRelativeLevelNotFoundException;
import com.ssafy.s10p31s102be.techmap.exception.TechMainCategoryNotFoundException;
import com.ssafy.s10p31s102be.techmap.exception.TechStatusNotFoundException;
import com.ssafy.s10p31s102be.techmap.exception.TechmapNotFoundException;
import com.ssafy.s10p31s102be.techmap.exception.TechmapProjectAlreadyExist;
import com.ssafy.s10p31s102be.techmap.exception.TechmapProjectNotFoundException;
import com.ssafy.s10p31s102be.techmap.infra.entity.Techmap;
import com.ssafy.s10p31s102be.techmap.infra.entity.TechmapProject;
import com.ssafy.s10p31s102be.techmap.infra.entity.TechmapProjectCompany;
import com.ssafy.s10p31s102be.techmap.infra.entity.TechmapProjectLab;
import com.ssafy.s10p31s102be.techmap.infra.entity.TechMainCategory;
import com.ssafy.s10p31s102be.techmap.infra.entity.TechmapProjectMember;
import com.ssafy.s10p31s102be.techmap.infra.entity.TechmapProjectProfile;
import com.ssafy.s10p31s102be.techmap.infra.enums.MoveStatus;
import com.ssafy.s10p31s102be.techmap.infra.enums.TechCompanyRelativeLevel;
import com.ssafy.s10p31s102be.techmap.infra.enums.TechStatus;
import com.ssafy.s10p31s102be.techmap.infra.repository.TechmapJpaRepository;
import com.ssafy.s10p31s102be.techmap.infra.repository.TechmapProjectJpaRepository;
import com.ssafy.s10p31s102be.techmap.infra.repository.TechmapProjectProfileRepositoryImpl;
import com.ssafy.s10p31s102be.techmap.infra.repository.TechMainCategoryJpaRepository;
import java.io.IOException;
import java.time.Year;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.ssafy.s10p31s102be.techmap.infra.entity.QTechmapProjectMember.techmapProjectMember;

@Service
@RequiredArgsConstructor
@Transactional
public class TechmapProjectServiceImpl implements TechmapProjectService {
    private final TechmapProjectJpaRepository techmapProjectRepository;
    private final MemberJpaRepository memberRepository;
    private final TechmapJpaRepository techmapRepository;
    private final TechMainCategoryJpaRepository techMainCategoryRepository;
    private final KeywordJpaRepository keywordRepository;
    private final ProfileJpaRepository profileRepository;
    private final CompanyJpaRepository companyRepository;
    private final TechmapProjectProfileRepositoryImpl techmapProjectProfileRepository;

    private final CompanyService companyService;

    private final ExcelUtil excelUtil;
    private final KeywordType KEYWORD_IDENTIFIER = KeywordType.TECH_SKILL;

    /*
        모든 멤버들은 techmap에 신규 기술 분야를 등록할 수 있다.
     */
    @Override
    public TechmapProject create(UserDetailsImpl userDetails, TechmapProjectCreateDto techmapProjectCreateDto) {
        Member member = memberRepository.findById(userDetails.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException(userDetails.getMemberId(), this));

        Techmap techmap = techmapRepository.findById(techmapProjectCreateDto.getTechmapId())
                .orElseThrow(() -> new TechmapNotFoundException(techmapProjectCreateDto.getTechmapId(), this));

        // mainCategory부터 조회
        TechMainCategory techMainCategory = techMainCategoryRepository.findById(techmapProjectCreateDto.getMainCategoryId())
                .orElseThrow(() -> new TechMainCategoryNotFoundException(techmapProjectCreateDto.getMainCategoryId(), this));

        // 기술키워드 조회
        Keyword keyword = keywordRepository.findByTypeAndData(KEYWORD_IDENTIFIER, techmapProjectCreateDto.getKeyword())
                .orElseGet(() -> new Keyword(KEYWORD_IDENTIFIER, techmapProjectCreateDto.getKeyword()));
        keywordRepository.save(keyword);
        keyword.use();

        // 당해년도 타겟 여부가 체크되지 않았다면 해당 target year는 없는 것으로 취급한다.
        Integer targetYear = -1;
        if (techmapProjectCreateDto.getTargetStatus()) {
            targetYear = Year.now().getValue();
        }

        // 해당 부서에 기술이 완전히 동일한 프로젝트가 이미 있다면 등록이 불가능하다.
        List<Tech> teches = checkTech(techmap, userDetails.getDepartmentId());

        Tech tech = new Tech(userDetails.getDepartmentId(), techMainCategory.getTechMainCategoryName(), techmapProjectCreateDto.getSubCategory(), keyword.getData());

        if (teches.contains(tech)) {
            throw new TechmapProjectAlreadyExist(keyword.getData(), this);
        }

        TechmapProject techmapProject = TechmapProject.builder()
                .techMainCategory(techMainCategory)
                .techSubCategory(techmapProjectCreateDto.getSubCategory())
                .keyword(keyword)
                .techStatus(techmapProjectCreateDto.getTechStatus())
                .description(techmapProjectCreateDto.getDescription())
                .techCompanyRelativeLevel(techmapProjectCreateDto.getRelativeLevel())
                .relativeLevelReason(techmapProjectCreateDto.getRelativeLevelReason())
                .targetMemberCount(techmapProjectCreateDto.getTargetMemberCount())
                .targetYear(targetYear)
                .targetStatus(techmapProjectCreateDto.getTargetStatus())
                .departmentId(member.getDepartment().getId())
                .manager(member)
                .techmap(techmap)
                .build();

        // 신규 기술 등록자는 우선적으로 담당자로 배정된다.
        techmapProject.getTechmapProjectMembers().add(new TechmapProjectMember(techmapProject, member));

        techmapProjectRepository.save(techmapProject);

        return techmapProject;
    }

    /*
        ToDo : DB indexing 문제 해결 후  saveAll로 변경 예정(최적화)
        기술 구분(TechStatus): Emerging(EMERGING), 신규(NEW), 기존(EXISTING)
        당사 수준(TechCompanyRelativeLevel): 우세(SUPERIOR), 동등(NORMAL), 열세(OUTNUMBERED)
     */
    @Override
    public Integer excelUpload(UserDetailsImpl userDetails, Integer techmapId, MultipartFile file) throws IOException {
        Techmap techmap = techmapRepository.findById(techmapId)
                .orElseThrow(() -> new TechmapNotFoundException(techmapId, this));

        List<Tech> teches = checkTech(techmap);

        List<Map<String, String>> techmapProjects = excelUtil.excelToMap(file);

        int failCnt = 0;
        int size = techmapProjects.size()-1;
        for (int i = size; i >= 0; i--) {
            Map<String, String> map  = techmapProjects.get(i);
            // 대분류 조회
            String techMainCategoryName = map.get("대분류");
            TechMainCategory techMainCategory = techMainCategoryRepository.findByTechMainCategoryName(techMainCategoryName)
                    .orElseThrow(() -> new TechMainCategoryNotFoundException(techMainCategoryName, this));

            String techSkill = map.get("세부기술");
            // 기술키워드 조회
            Keyword keyword = keywordRepository.findByTypeAndData(KEYWORD_IDENTIFIER, techSkill)
                    .orElseGet(() -> new Keyword(KEYWORD_IDENTIFIER, techSkill));
            keywordRepository.save(keyword);
            keyword.use();

            Boolean targetStatus = isTrue(map.get("타겟 분야"));
            Integer targetYear = -1;
            if (targetStatus) {
                targetYear = techmap.getTargetYear();
            }

            // 담당자 조회
            String knoxId = map.get("담당자(Knox ID)");
            Member manager = memberRepository.findByKnoxId(knoxId)
                    .orElseThrow(() -> new MemberNotFoundException(knoxId, this));

            // 운영진이나 관리자가 아닐 때, 다른 부서 사람을 담당자로 하는 인재Pool 프로젝트 생성이 불가능하다.
            if (userDetails.getAuthorityLevel() > 2 && !manager.getDepartment().getId().equals(userDetails.getDepartmentId())) {
                continue;
            }

            // 이미 등록된게 있으면 등록을 하지않고 없으면 teches에 추가해 중복 등록 방지
            Tech newTech = new Tech(manager.getDepartment().getId(), techMainCategoryName, map.get("기술분야"), techSkill);
            if (teches.contains(newTech)) {
                failCnt++;
                continue;
            } else {
                teches.add(newTech);
            }

            TechmapProject techmapProject = TechmapProject.builder()
                    .techMainCategory(techMainCategory)
                    .techSubCategory(map.get("기술분야"))
                    .keyword(keyword)
                    .techStatus(techStatus(map.get("기술구분")))
                    .description(map.get("기술 개요 및 필요 역량"))
                    .techCompanyRelativeLevel(techCompanyRelativeLevel(map.get("당사수준")))
                    .relativeLevelReason(map.get("판단 근거"))
                    .targetMemberCount(map.get("확보 목표").equals("-") ? 0 : Integer.parseInt(map.get("확보 목표")))
                    .targetStatus(targetStatus)
                    .targetYear(targetYear)
                    .departmentId(manager.getDepartment().getId())
                    .manager(manager)
                    .techmap(techmap)
                    .build();

            // 신규 기술 등록자는 우선적으로 담당자로 배정된다.
            techmapProject.getTechmapProjectMembers().add(new TechmapProjectMember(techmapProject, manager));

            // 회사 조회
            List<TechmapProjectCompany> techmapProjectCompany = techmapProject.getTechmapProjectCompanies();
            String[] companies = map.get("회사").split("\n");
            for (String companyName : companies) {
                companyName = companyName.trim();
                if (!companyName.isEmpty()) {
                    String finalCompanyName = companyName;
                    Company company = companyRepository.findByName(companyName)
                            .orElseGet(() -> companyService.companyCreate(new CompanyCreateDto(finalCompanyName)));

                    techmapProjectCompany.add(new TechmapProjectCompany(techmapProject, company));
                }
            }

            techmapProjectRepository.save(techmapProject);
        }

        return failCnt;
    }


    private Boolean isTrue(String word) {
        if (word.equals("예")) {
            return true;
        } else {
            return false;
        }
    }

    private TechStatus techStatus(String word) {
        if (word.equals("신규")) {
            return TechStatus.NEW;
        } else if (word.equals("기존")) {
            return TechStatus.EXISTING;
        } else if (word.equals("기타")) {
            return TechStatus.ETC;
        } else {
            throw new TechStatusNotFoundException(word, this);
        }
    }

    private TechCompanyRelativeLevel techCompanyRelativeLevel(String word) {
        if (word.equals("우세")) {
            return TechCompanyRelativeLevel.SUPERIOR;
        } else if (word.equals("동등")) {
            return TechCompanyRelativeLevel.NORMAL;
        } else if (word.equals("열세")) {
            return TechCompanyRelativeLevel.OUTNUMBERED;
        } else {
            throw new TechCompanyRelativeLevelNotFoundException(word, this);
        }
    }

    /*
        출력하고 싶은 인재Pool 프로젝트를 체크 후 ID 값 기반으로 탐색해 엑셀화 시켜 파일을 내려주기 위한 기능
     */
    @Override
    public ByteArrayResource excelDownload(UserDetailsImpl userDetails, List<Integer> techmapProjectIds) throws IOException {
        List<Map<String, String>> dataList = new ArrayList<>();

        for (Integer techmapProjectId : techmapProjectIds) {
            TechmapProject techmapProject = techmapProjectRepository.findById(techmapProjectId)
                    .orElseThrow(() -> new TechmapProjectNotFoundException(techmapProjectId, this));

            Map<String, String> map = new LinkedHashMap<>();

            map.put("대분류", techmapProject.getTechMainCategory().getTechMainCategoryName());
            map.put("기술분야", techmapProject.getTechSubCategory());
            map.put("세부기술", techmapProject.getKeyword().getData());
            map.put("기술 개요 및 필요 역량", techmapProject.getDescription());
            map.put("기술구분", techStatus(techmapProject.getTechStatus())); // Assuming TechStatus is an enum
            map.put("당사수준", techCompanyRelativeLevel(techmapProject.getTechCompanyRelativeLevel())); // Assuming TechCompanyRelativeLevel is an enum
            map.put("판단 근거", techmapProject.getRelativeLevelReason());
            map.put("타겟 분야", targetStatus(techmapProject.getTargetStatus()));
            map.put("확보 목표", String.valueOf(techmapProject.getTargetMemberCount()));
            map.put("인재 Pool 사이즈", String.valueOf(techmapProject.getTechmapProjectProfiles().size()));
            map.put("연구실", String.valueOf(techmapProject.getTechmapProjectLabs().size()));
            map.put("회사", String.join("\n", techmapProject.getTechmapProjectCompanies().stream().map(companies -> companies.getCompany().getName()).collect(Collectors.toList())));
            map.put("담당자(Knox ID)", techmapProject.getManager().getKnoxId());

            dataList.add(map);
        }

        return new ByteArrayResource(excelUtil.mapToExcel(dataList));
    }

    private String targetStatus(Boolean status){
        if(status){
            return "예";
        } else{
            return "아니오";
        }
    }

    private String techStatus(TechStatus techStatus) {
        if (techStatus.equals(TechStatus.EXISTING)){
            return "기존";
        } else if (techStatus.equals(TechStatus.NEW)) {
            return "신규";
        } else if (techStatus.equals(TechStatus.ETC)) {
            return "기타";
        } else{
            return "디폴트";
        }
    }

    private String techCompanyRelativeLevel(TechCompanyRelativeLevel techCompanyRelativeLevel) {
        if (techCompanyRelativeLevel.equals(TechCompanyRelativeLevel.NORMAL)){
            return "동등";
        } else if (techCompanyRelativeLevel.equals(TechCompanyRelativeLevel.OUTNUMBERED)){
            return "열세";
        } else if (techCompanyRelativeLevel.equals(TechCompanyRelativeLevel.SUPERIOR)){
            return "우세";
        } else{
            return "디폴트";
        }
    }

    /*
        관리자, 운영진은 모든 인재Pool 프로젝트를 조회 가능하다. (전체 프로젝트 조회 default)
        나머지 부서의 채용담당자들은 자신이 속한 부서의 인재Pool 프로젝트만을 조회 가능하다.
     */
    @Override
    public TechmapProjectPageDto findtechmapProjects(UserDetailsImpl userDetails, TechmapProjectFindDto techmapProjectFindDto) {
        Pageable pageable = PageRequest.of(techmapProjectFindDto.getPageNumber(), techmapProjectFindDto.getSize(), Sort.by(Sort.Direction.DESC, "id"));
        Integer techmapId = techmapProjectFindDto.getTechmapId();
        TechCompanyRelativeLevel techCompanyRelativeLevel = techmapProjectFindDto.getTechCompanyRelativeLevel();
        Integer targetYear = techmapProjectFindDto.getTargetYear();
        TechStatus techStatus = techmapProjectFindDto.getTechStatus();
        Boolean targetStatus = techmapProjectFindDto.getTargetStatus();
        Integer departmentId = techmapProjectFindDto.getDepartmentId();

        // 부서 별로 조회를 원한다면 관리자와 운영진만 가능하다.
        if (departmentId != null && userDetails.getAuthorityLevel() > 2) {
            throw new InvalidAuthorizationException(userDetails.getMemberId(), this);
        }

        if (userDetails.getAuthorityLevel() > 2) {
            departmentId = userDetails.getDepartmentId();
        }

        Page<TechmapProject> pages = techmapProjectRepository.findtechmapProjects(pageable, techmapId, techCompanyRelativeLevel, targetYear, techStatus, targetStatus, departmentId);
        List<TechmapProjectReadDto> techmapProjects = pages.getContent().stream()
                .map(TechmapProjectReadDto::fromEntity).toList();

        return new TechmapProjectPageDto(techmapProjects, searchTargetYear(departmentId), pages.getTotalPages(), pages.getTotalElements(), pages.getNumber(), pages.getSize());

    }

    private List<TechmapTargetYearReadDto> searchTargetYear(Integer departmentId) {
        List<TechmapTargetYearReadDto> list = new ArrayList<>();

        List<Object[]> results = techmapRepository.findTargetYear(departmentId);
        for (Object[] result : results) {
            Integer targetYear = (Integer) result[0];
            Long count = (Long) result[1];
            List<Long> techmapIds = techmapRepository.findtechmapIdsByTargetYear(targetYear);

            list.add(new TechmapTargetYearReadDto(targetYear, count, techmapIds));
        }

        return list;
    }

    /*
        인재Pool 프로젝트를 만든 당사자만 수정이 가능하며 다른 멤버가 해당 인재Pool 프로젝트를 수정을 원하다면 담당자보다 높은 권한을 가지고 있어야한다.
     */
    @Override
    public TechmapProject update(UserDetailsImpl userDetails, Integer techmapProjectId, TechmapProjectCreateDto techmapProjectCreateDto) {
        TechmapProject techmapProject = techmapProjectRepository.findById(techmapProjectId)
                .orElseThrow(() -> new TechmapProjectNotFoundException(techmapProjectId, this));

        Member manager = techmapProject.getManager();
        if (isValid(manager, userDetails)) {
            // mainCategory부터 조회
            TechMainCategory techMainCategory = techMainCategoryRepository.findById(techmapProjectCreateDto.getMainCategoryId())
                    .orElseThrow(() -> new TechMainCategoryNotFoundException(techmapProjectCreateDto.getMainCategoryId(), this));

            // 기술키워드 조회
            Keyword keyword = keywordRepository.findByTypeAndData(KEYWORD_IDENTIFIER, techmapProjectCreateDto.getKeyword())
                    .orElseGet(() -> new Keyword(KEYWORD_IDENTIFIER, techmapProjectCreateDto.getKeyword()));
            keyword.use();

            techmapProject.updatetechmapProject(techmapProjectCreateDto.getSubCategory(), techmapProjectCreateDto.getTechStatus(), techmapProjectCreateDto.getDescription(), techmapProjectCreateDto.getRelativeLevel(), techmapProjectCreateDto.getRelativeLevelReason(),
                    techmapProjectCreateDto.getTargetStatus(), techmapProjectCreateDto.getTargetMemberCount(),
                    techMainCategory, keyword);

            techmapProjectRepository.save(techmapProject);

            return techmapProject;
        } else {
            throw new InvalidAuthorizationException(userDetails.getMemberId(), this);
        }
    }

    @Override
    public TechmapProject updateManager(UserDetailsImpl userDetails, Integer techmapProjectId, Integer managerId) {
        TechmapProject techmapProject = techmapProjectRepository.findById(techmapProjectId)
                .orElseThrow(() -> new TechmapProjectNotFoundException(techmapProjectId, this));

        Member manager = techmapProject.getManager();

        /*
            원래 담당자보다 높은 권한의 멤버나 기존 담당자 자신만 담당자 변경이 가능하다.
         */
        if (isValid(manager, userDetails)){
            Member newManager = memberRepository.findById(managerId)
                    .orElseThrow(() -> new MemberNotFoundException(managerId, this));

            techmapProject.updateManager(newManager);
        }

        techmapProjectRepository.save(techmapProject);

        return techmapProject;
    }


    /*
        인재Pool 프로젝트를 만든 당사자 혹은 그 위의 권한을 가진 사람만 복사, 이동이 가능하다
        새로운 인재Pool을 배정받았을 때 기존 인재Pool 프로젝트로 만들어 둔 것을 새로운 인재Pool에 복사할 수 있는 기능
     */
    @Override
    public void duplicatetechmapProject(UserDetailsImpl userDetails, TechmapProjectDuplicateDto techmapProjectDuplicateDto) {
        Integer targettechmapId = techmapProjectDuplicateDto.getTechmapId();
        MoveStatus moveStatus = techmapProjectDuplicateDto.getMoveStatus();

        List<Integer> techmapProjectIds = techmapProjectDuplicateDto.getTechmapProjectId();

        for (Integer techmapProjectId : techmapProjectIds) {
            TechmapProject techmapProject = techmapProjectRepository.findById(techmapProjectId)
                    .orElseThrow(() -> new TechmapProjectNotFoundException(techmapProjectId, this));

            Member manager = techmapProject.getManager();
            // 채용 담당자부터 복사 이동이 가능하도록 수정
            if (userDetails.getAuthorityLevel() < 5) {
                Techmap targettechmap = techmapRepository.findById(targettechmapId)
                        .orElseThrow(() -> new TechmapNotFoundException(targettechmapId, this));

                // targettechmap에 이미 해당 기술을 포함하는게 있으면 이동 불가
                List<Tech> teches = checkTech(targettechmap, userDetails.getDepartmentId());

                Tech tech = new Tech(userDetails.getDepartmentId(), techmapProject.getTechMainCategory().getTechMainCategoryName(), techmapProject.getTechSubCategory(), techmapProject.getKeyword().getData());

                if (teches.contains(tech)) {
                    throw new TechmapProjectAlreadyExist(techmapProject.getKeyword().getData(), this);
                }

                TechmapProject newtechmapProject = techmapProject.builder()
                        .techMainCategory(techmapProject.getTechMainCategory())
                        .techSubCategory(techmapProject.getTechSubCategory())
                        .keyword(techmapProject.getKeyword())
                        .techStatus(moveStatus.equals(MoveStatus.DUPLICATE) && techmapProject.getTargetYear() < targettechmap.getTargetYear() && techmapProject.getTechStatus().equals(TechStatus.NEW)
                                ? TechStatus.EXISTING : techmapProject.getTechStatus())
                        .description(techmapProject.getDescription())
                        .techCompanyRelativeLevel(techmapProject.getTechCompanyRelativeLevel())
                        .relativeLevelReason(techmapProject.getRelativeLevelReason())
                        .targetMemberCount(techmapProject.getTargetMemberCount())
                        .targetYear(targettechmap.getTargetYear())
                        .targetStatus(techmapProject.getTargetStatus())
                        .departmentId(manager.getDepartment().getId())
                        .manager(manager)
                        .techmap(targettechmap)
                        .build();

                // techLab과 techCompany도 전부 복사
                List<TechmapProjectCompany> newtechmapProjectCompanies = newtechmapProject.getTechmapProjectCompanies();
                List<TechmapProjectCompany> techmapProjectCompanies = techmapProject.getTechmapProjectCompanies();

                for (TechmapProjectCompany techmapProjectCompany : techmapProjectCompanies) {
                    newtechmapProjectCompanies.add(new TechmapProjectCompany(newtechmapProject, techmapProjectCompany.getCompany()));
                }

                List<TechmapProjectLab> newtechmapProjectLabs = newtechmapProject.getTechmapProjectLabs();
                List<TechmapProjectLab> TechmapProjectLabs = techmapProject.getTechmapProjectLabs();

                for (TechmapProjectLab techmapProjectLab : TechmapProjectLabs) {
                    newtechmapProjectLabs.add(new TechmapProjectLab(newtechmapProject, techmapProjectLab.getLab()));
                }

                // 이동이라면 해당 인재Pool 프로젝트는 기존 인재Pool에서 삭제되어야 한다.
                if (moveStatus.equals(MoveStatus.MOVE)) {
                    techmapProjectRepository.delete(techmapProject);
                }

                // 신규 기술 등록자는 우선적으로 담당자로 배정된다.
                newtechmapProject.getTechmapProjectMembers().add(new TechmapProjectMember(newtechmapProject, manager));

                techmapProjectRepository.save(newtechmapProject);
            } else {
                throw new InvalidAuthorizationException(userDetails.getMemberId(), this);
            }
        }
    }

    /*
        인재Pool 프로젝트에서 만든 인재 Pool을 가져올 수 있다.
     */
    @Override
    public TechmapProjectProfilePageDto findProfiles(Integer techmapProjectId, TechmapProjectProfileFindDto techmapProjectProfileFindDto) {
        Pageable pageable = PageRequest.of(techmapProjectProfileFindDto.getPageNumber(), techmapProjectProfileFindDto.getSize(), Sort.by(Sort.Direction.DESC, "id"));

        Page<Profile> pages = techmapProjectProfileRepository.findProfilesBytechmapProjectConditions(techmapProjectId, pageable);
        List<ProfilePreviewDto> profilePreviews = pages.getContent().stream()
                .map(ProfilePreviewDto::fromEntity).toList();

        return TechmapProjectProfilePageDto.fromEntity(profilePreviews, pages);
    }

    /*
        인재Pool 프로젝트를 만든 담당자와 관리자는 인재 Pool에 인재를 추가할 수 있다.
     */
    @Override
    public TechmapProject updateProfiles(UserDetailsImpl userDetails, Integer techmapProjectId, List<Integer> profileIds) {
        TechmapProject techmapProject = techmapProjectRepository.findById(techmapProjectId)
                .orElseThrow(() -> new TechmapProjectNotFoundException(techmapProjectId, this));

        List<TechmapProjectProfile> techmapProjectProfiles = techmapProject.getTechmapProjectProfiles();
        techmapProjectProfiles.removeIf(rp -> profileIds.contains(rp.getProfile().getId()));

        for (Integer profileId : profileIds) {
            Profile profile = profileRepository.findById(profileId)
                    .orElseThrow(() -> new ProfileNotFoundException(profileId, this));

            techmapProjectProfiles.add(new TechmapProjectProfile(techmapProject, profile));
        }

        techmapProjectRepository.save(techmapProject);

        return techmapProject;
    }

    @Override
    public TechmapProject deleteProfiles(UserDetailsImpl userDetails, Integer techmapProjectId, List<Integer> profileIds) {
        TechmapProject techmapProject = techmapProjectRepository.findById(techmapProjectId)
                .orElseThrow(() -> new TechmapProjectNotFoundException(techmapProjectId, this));

        List<TechmapProjectProfile> techmapProjectProfiles = techmapProject.getTechmapProjectProfiles();
        techmapProjectProfiles.removeIf(rp -> profileIds.contains(rp.getProfile().getId()));

        techmapProjectRepository.save(techmapProject);

        return techmapProject;
    }

    /*
        관리자와 인재Pool 프로젝트를 만든 당사자는 해당 프로젝트 삭제가 가능하다.
     */
    @Override
    public void delete(UserDetailsImpl userDetails, List<Integer> techmapProjectIds) {
        // mainCategory부터 조회
        for (Integer techmapProjectId : techmapProjectIds) {
            TechmapProject techmapProject = techmapProjectRepository.findById(techmapProjectId)
                    .orElseThrow(() -> new TechmapProjectNotFoundException(techmapProjectId, this));

            Member manager = techmapProject.getManager();
            if (isValid(manager, userDetails)) {

                techmapProjectRepository.deleteById(techmapProjectId);

            } else {
                throw new InvalidAuthorizationException(userDetails.getMemberId(), this);
            }
        }
    }

    // 등록한 작성자보다 권한이 높거나 당사자만 접근 가능 여부를 확인
    private boolean isValid(Member manager, UserDetailsImpl userDetails) {
        if (manager.getAuthority().getAuthLevel() > userDetails.getAuthorityLevel() || manager.getId().equals(userDetails.getMemberId())) {
            return true;
        } else {
            throw new InvalidAuthorizationException(manager.getId(), this);
        }
    }

    // 등록 시 사용하는 중복 검사 로직
    private List<Tech> checkTech(Techmap techmap, Integer departmentId) {
        List<TechmapProject> techmapProjects = techmapProjectRepository.findtechmapProjectsByDepartmentIdAndtechmap(departmentId, techmap);
        List<Tech> teches = new ArrayList<>();
        for (TechmapProject techmapProject : techmapProjects) {
            Tech existingTech = new Tech(techmapProject.getDepartmentId(), techmapProject.getTechMainCategory().getTechMainCategoryName(), techmapProject.getTechSubCategory(), techmapProject.getKeyword().getData());
            teches.add(existingTech);
        }

        return teches;
    }

    // 엑셀 등록 시 사용하는 중복 검사 로직
    private List<Tech> checkTech(Techmap techmap) {
        List<Tech> teches = new ArrayList<>();

        List<TechmapProject> techmapProjects = techmap.getTechmapProjects();
        for (TechmapProject techmapProject : techmapProjects) {
            Tech existingTech = new Tech(techmapProject.getDepartmentId(), techmapProject.getTechMainCategory().getTechMainCategoryName(), techmapProject.getTechSubCategory(), techmapProject.getKeyword().getData());
            teches.add(existingTech);
        }

        return teches;
    }

    private class Tech {
        Integer departmentId;
        String mainTech;
        String subTech;
        String techDetail;

        Tech(Integer departmentId, String mainTech, String subTech, String techDetail) {
            this.departmentId = departmentId;
            this.mainTech = mainTech;
            this.subTech = subTech;
            this.techDetail = techDetail;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tech tech = (Tech) o;
            return Objects.equals(departmentId, tech.departmentId) && Objects.equals(mainTech, tech.mainTech) && Objects.equals(subTech, tech.subTech) && Objects.equals(techDetail, tech.techDetail);
        }

        @Override
        public int hashCode() {
            return Objects.hash(departmentId, mainTech, subTech, techDetail);
        }
    }
}
