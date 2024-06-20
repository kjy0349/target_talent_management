package com.ssafy.s10p31s102be.common.service;


import com.ssafy.s10p31s102be.common.exception.FileEmptyException;
import com.ssafy.s10p31s102be.profile.dto.request.ProfileCreateDto;
import com.ssafy.s10p31s102be.profile.service.ProfileService;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ExcelServiceImpl implements ExcelService {
    private final ProfileService profileService;

    /*
        ToDO: 현재 담당자 Member 1번으로 고정, 추후 member 관련 로직 추가 예정 -> Knox ID를 이용한 담당자 지정 방식 사용 예정
     */
    @Override
    public void excelImport(MultipartFile file) throws IOException {
        List<ProfileCreateDto> profiles = excelToProfile(file.getInputStream());
        for (ProfileCreateDto profileCreateDto : profiles) {
            profileService.create(null, profileCreateDto);
        }
    }

    private List<ProfileCreateDto> excelToProfile(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
            DataFormatter formatter = new DataFormatter();

            Sheet sheet = workbook.getSheet("Sheet1");
            Iterator<Row> rows = sheet.iterator();

            List<ProfileCreateDto> profiles = new ArrayList<>();
            List<String> columnKeys = new ArrayList<>();

            int rowNum = 0;
            while (rows.hasNext()) {
                Row curRow = rows.next();
                Iterator<Cell> cells = curRow.cellIterator();
                // 맨 처음 줄은 넣고 싶은 Profile의 Column 명을 뜻한다.
                if (rowNum == 0) {
                    while (cells.hasNext()) {
                        Cell curCell = cells.next();
                        columnKeys.add(curCell.getStringCellValue());
                    }
                    rowNum++;
                } else {
                    Map<String, String> profileColumn = new HashMap<>();
                    // 프로필 칼럼의 data 값들을 받는 줄
                    for (int curCellIdx = 0; curCellIdx < columnKeys.size(); curCellIdx++) {
                        // 현재 셀의 데이터를 가져오거나 빈 칼럼 처리
                        if (cells.hasNext()) {
                            Cell curCell = cells.next();
                            profileColumn.put(columnKeys.get(curCellIdx), formatter.formatCellValue(curCell));
                        } else {
                            // 빈 칼럼에 대해 빈 문자열 저장
                            profileColumn.put(columnKeys.get(curCellIdx), "");
                        }
                    }
                    profiles.add(ProfileCreateDto.builder()
                            .columns(profileColumn)
                            .build());
                }
            }

            workbook.close();

            return profiles;
        } catch (Exception e) {
            throw new FileEmptyException("fail to parse file", this);
        }
    }
}
