package com.ssafy.s10p31s102be.common.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@Slf4j
public class ExcelUtil {
    public List<Map<String, String>> excelToMap(MultipartFile file) throws IOException {
        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            List<Map<String, String>> rowDataList = new ArrayList<>();
            DataFormatter formatter = new DataFormatter();
            List<String> columnKeys = new ArrayList<>();

            int rowNum = 0;
            while (rows.hasNext()) {
                Row curRow = rows.next();
                if (rowNum == 0) {
                    // First row contains the column headers
                    for (Cell cell : curRow) {
                        columnKeys.add(cell.getStringCellValue());
                    }
                    rowNum++;
                } else {
                    Map<String, String> rowMap = new LinkedHashMap<>();
                    Iterator<Cell> cells = curRow.cellIterator();
                    for (int curCellIdx = 0; curCellIdx < columnKeys.size(); curCellIdx++) {
                        Cell cell = curRow.getCell(curCellIdx, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        String cellValue = (cell != null) ? formatter.formatCellValue(cell) : "";
                        if (curCellIdx == 0 && cellValue.isBlank()) {
                            return rowDataList;
                        }
                        rowMap.put(columnKeys.get(curCellIdx), cellValue);
                    }
                    rowDataList.add(rowMap);
                }
            }
            return rowDataList;
        }
    }

    public byte[] mapToExcel(List<Map<String, String>> dataList) throws IOException {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Data");

            Row headerRow = sheet.createRow(0);
            if (!dataList.isEmpty()) {
                // headerRow에 들어갈 key값들을 미리 세팅한다.
                Set<String> keys = dataList.get(0).keySet();
                int cellNum = 0;
                for (String key : keys) {
                    Cell cell = headerRow.createCell(cellNum++);
                    cell.setCellValue(key);
                }

                // 2번째 row부터 data를 작성한다.
                int rowNum = 1;
                for (Map<String, String> rowData : dataList) {
                    Row row = sheet.createRow(rowNum++);
                    cellNum = 0;
                    for (String key : keys) {
                        Cell cell = row.createCell(cellNum++);
                        cell.setCellValue(rowData.getOrDefault(key, ""));
                    }
                }
            }

            workbook.write(bos);
            return bos.toByteArray();
        }
    }
}
