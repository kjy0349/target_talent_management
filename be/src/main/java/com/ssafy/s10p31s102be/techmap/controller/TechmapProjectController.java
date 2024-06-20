package com.ssafy.s10p31s102be.techmap.controller;

import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.techmap.dto.request.TechmapProjectCreateDto;
import com.ssafy.s10p31s102be.techmap.dto.request.TechmapProjectDuplicateDto;
import com.ssafy.s10p31s102be.techmap.dto.request.TechmapProjectFindDto;
import com.ssafy.s10p31s102be.techmap.dto.request.TechmapProjectListDto;
import com.ssafy.s10p31s102be.techmap.dto.request.TechmapProjectProfileFindDto;
import com.ssafy.s10p31s102be.techmap.dto.response.TechmapProjectPageDto;
import com.ssafy.s10p31s102be.techmap.dto.response.TechmapProjectProfilePageDto;
import com.ssafy.s10p31s102be.techmap.service.TechmapProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Tag(name = "신규 기술 분야")
@RestController
@RequiredArgsConstructor
@RequestMapping("/techmap-project")
public class TechmapProjectController {
    private final TechmapProjectService techmapProjectService;

    @Operation(summary = "신규 기술 분야 생성", description = "모든 멤버들은 techmap에 신규 기술 분야를 등록할 수 있다.")
    @PostMapping
    public ResponseEntity<Void> createtechmapProject(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                     @RequestBody TechmapProjectCreateDto techmapProjectCreateDto) {
        techmapProjectService.create(userDetails, techmapProjectCreateDto);

        return ResponseEntity.ok().build();
    }

    /*
        엑셀 파일 업로드 시 DB에 저장하기 위한 기능
     */
    @Operation(summary = "신규 기술 분야 엑셀 업로드", description = "엑셀 파일 업로드 시 DB에 저장하기 위한 기능")
    @PostMapping("/excel-upload/{techmapId}")
    public ResponseEntity<String> uploadtechmapProject(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                     @PathVariable("techmapId") Integer techmapId,
                                                     @RequestPart("file") MultipartFile file) throws IOException {
        Integer failCnt = techmapProjectService.excelUpload(userDetails, techmapId, file);

        if (failCnt == 0){
            return ResponseEntity.ok().body("업로드에 성공했습니다.");
        } else{
            return ResponseEntity.ok().body("기존 등록된 내용과 중복된 내용이 확인되어, " + failCnt + " 건이 업로드하지 못했습니다.");
        }
    }

    /*
        엑셀 파일 다운로드 시 DB에서 값을 꺼내오기 위한 기능
     */
    @Operation(summary = "신규 기술 분야 엑셀 다운로드", description = "엑셀 파일 다운로드 시 DB에서 값을 꺼내오기 위한 기능")
    @PostMapping("/excel-download")
    public ResponseEntity<StreamingResponseBody> downloadExcel(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                               @RequestBody TechmapProjectListDto techmapProjectListDto) throws IOException {
        List<Integer> techmapProjectIds = techmapProjectListDto.getTechmapProjectIds();
        ByteArrayResource excelFile = techmapProjectService.excelDownload(userDetails, techmapProjectIds);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=data.xlsx")
                .body(outputStream -> outputStream.write(excelFile.getByteArray()));
    }

    /*
        운영진 혹은 관리자가 techmapProject를 보기 원한다면 전체 부서를 보여준다.
        그 외에 부서 별 담당자가 조회 시 자신이 속한 부서 프로젝트만 조회가 가능하다.
     */
    @Operation(summary = "신규 기술 분야 조회", description = "신규 기술 분야 조회")
    @PostMapping("/list")
    public ResponseEntity<TechmapProjectPageDto> findtechmapProjects(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                     @Valid @RequestBody TechmapProjectFindDto techmapProjectFindDto) {
        TechmapProjectPageDto techmapProjects = techmapProjectService.findtechmapProjects(userDetails, techmapProjectFindDto);

        return ResponseEntity.ok().body(techmapProjects);
    }

    /*
        운영진이 새로운 인재Pool을 배정했을 때 진행하던 인재Pool 프로젝트를 새로운 인재Pool에 복사 이동할 수 있다.
     */
    @Operation(summary = "신규 기술 분야 복사 이동 기능", description = "신규 기술 분야를 새롭게 만들어진 인재Pool으로 복사 혹은 이동이 가능하다.")
    @PostMapping("/move")
    public ResponseEntity<Void> duplicatetechmapProject(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                        @RequestBody TechmapProjectDuplicateDto techmapProjectDuplicateDto) {
        techmapProjectService.duplicatetechmapProject(userDetails, techmapProjectDuplicateDto);

        return ResponseEntity.ok().build();
    }

    /*
        인재Pool 프로젝트에 해당하는 인재 Pool을 조회 가능해야한다.
     */
    @Operation(summary = "신규 기술 분야 인재 Pool 조회", description = "신규 기술 분야 인재 Pool 조회")
    @PostMapping("/{techmapProjectId}/profiles")
    public ResponseEntity<TechmapProjectProfilePageDto> findProfilesBytechmapProjectId(@PathVariable("techmapProjectId") Integer techmapProjectId,
                                                                                       @RequestBody TechmapProjectProfileFindDto techmapProjectProfileFindDto) {
        return ResponseEntity.ok().body(techmapProjectService.findProfiles(techmapProjectId, techmapProjectProfileFindDto));
    }

    /*
        관리자와 인재Pool 프로젝트 담당자는 해당 인재Pool 프로젝트의 내용을 수정할 수 있다.
     */
    @Operation(summary = "신규 기술 분야 업데이트", description = "관리자와 인재Pool 프로젝트 담당자는 해당 인재Pool 프로젝트의 내용을 수정할 수 있다.")
    @PutMapping("/{techmapProjectId}")
    public ResponseEntity<Void> updatetechmap(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                              @PathVariable("techmapProjectId") Integer techmapProjectId,
                                              @RequestBody TechmapProjectCreateDto techmapProjectCreateDto) {
        techmapProjectService.update(userDetails, techmapProjectId, techmapProjectCreateDto);

        return ResponseEntity.ok().build();
    }

    /*
        인재Pool 프로젝트 담당자 당사자와 상위 권한의 사람은 프로젝트 담당자를 변경할 수 있다.
     */
    @Operation(summary = "신규 기술 분야 담당자 변경", description = "인재Pool 프로젝트 담당자 당사자와 상위 권한의 사람은 프로젝트 담당자를 같은 부서 인원으로 변경할 수 있다.")
    @PutMapping("/{techmapProjectId}/update-manager/{managerId}")
    public ResponseEntity<Void> updateManager(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                              @PathVariable("techmapProjectId") Integer techmapProjectId,
                                              @PathVariable("managerId") Integer managerId) {
        techmapProjectService.updateManager(userDetails, techmapProjectId, managerId);

        return ResponseEntity.ok().build();
    }

    /*
        관리자와 인재Pool 프로젝트 담당자는 해당 인재Pool 프로젝트의 내용을 수정할 수 있다.
     */
    @Operation(summary = "신규 기술 분야 등록된 인재 Pool 업데이트", description = "관리자와 인재Pool 프로젝트 담당자는 해당 인재Pool 프로젝트의 내용을 수정할 수 있다.")
    @PutMapping("/{techmapProjectId}/update-profile")
    public ResponseEntity<Void> updateProfiles(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                               @PathVariable("techmapProjectId") Integer techmapProjectId,
                                               @RequestBody List<Integer> profileIds) {
        techmapProjectService.updateProfiles(userDetails, techmapProjectId, profileIds);

        return ResponseEntity.ok().build();
    }

    /*
       관리자와 인재Pool 프로젝트 담당자는 인재Pool 프로젝트 내 인재를 삭제할 수 있다.
     */
    @Operation(summary = "신규 기술 분야 등록된 인재 삭제", description = "관리자와 인재Pool 프로젝트 담당자는 인재Pool 프로젝트 내 인재를 삭제할 수 있다.")
    @DeleteMapping("/{techmapProjectId}/update-profile")
    public ResponseEntity<Void> deleteProfiles(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                               @PathVariable("techmapProjectId") Integer techmapProjectId,
                                               @RequestBody List<Integer> profileIds){
        techmapProjectService.deleteProfiles(userDetails, techmapProjectId, profileIds);

        return ResponseEntity.ok().build();
    }

    /*
        관리자와 인재Pool 프로젝트 담당자는 해당 인재Pool 프로젝트의 내용을 삭제할 수 있다.
     */
    @Operation(summary = "신규 기술 분야 삭제", description = "관리자와 인재Pool 프로젝트 담당자는 해당 인재Pool 프로젝트의 내용을 수정할 수 있다.")
    @DeleteMapping
    public ResponseEntity<Void> deletetechmap(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                              @RequestBody List<Integer> techmapProjectIds) {
        techmapProjectService.delete(userDetails, techmapProjectIds);

        return ResponseEntity.ok().build();
    }
}