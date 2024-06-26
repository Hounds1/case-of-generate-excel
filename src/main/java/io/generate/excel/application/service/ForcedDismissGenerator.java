package io.generate.excel.application.service;

import io.generate.excel.application.domain.excel.DummyExcelObject;
import io.generate.excel.application.domain.report.MemoryUseCaseReport;
import io.generate.excel.application.utils.dummy.delivery.DummyExcelDeliver;
import io.generate.excel.application.utils.support.annotation.ExcelMeta;
import io.generate.excel.application.utils.support.annotation.IntegrationTestSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * Using
 * WorkBook : XSSFWorkbook
 * Streams : FileOutputStream, BufferedOutputStream
 * Check Size : byte -> MB
 * Test Invoke Count : 10
 * Dummy List Range = 10000
 * Average Consumed = 112.5 MB
 * Etc : Forced Dismiss All Objects
 *
 * IntelliJ Profiler Recorded
 * addCells : 596,172,832 (bytes) -> 568.96 (MB)
 * transferTo : 206,251,152 (bytes) -> 196.67 (MB)
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class ForcedDismissGenerator extends AbstractExcelGenerator {

    private final DummyExcelDeliver dummyExcelDeliver;

    @Override
    @IntegrationTestSupport
    public MemoryUseCaseReport generate() {
        MemoryUseCaseReport useCaseReport = new MemoryUseCaseReport();

        List<DummyExcelObject> dummyCells = dummyExcelDeliver.generate(1);

        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFSheet sheet = workbook.createSheet("TEST_SHEET");
        DummyExcelObject sampleObject = dummyCells.get(0);

        Map<Integer, String> headerCellMap = headerCellMap(sampleObject);

        XSSFRow headerRow = sheet.createRow(0);

        for (Map.Entry<Integer, String> entry : headerCellMap.entrySet()) {
            XSSFCell cell = headerRow.createCell(entry.getKey());

            cell.setCellValue(entry.getValue());
        }

        addCells(sheet, dummyCells);
        transferTo(workbook);

        workbook = null;
        dummyCells = null;
        headerCellMap = null;

        return useCaseReport;
    }

    private void addCells(XSSFSheet sheet, List<DummyExcelObject> dummyCells) {

        for (int i = 0; i < dummyCells.size(); i++) {
            XSSFRow contentRow = sheet.createRow(i + 1);

            DummyExcelObject content = dummyCells.get(i);

            Field[] declaredFields = content.getClass().getDeclaredFields();

            for (Field declaredField : declaredFields) {
                if (!declaredField.canAccess(content)) {
                    declaredField.setAccessible(true);
                }

                if (declaredField.isAnnotationPresent(ExcelMeta.class)) {
                    ExcelMeta presentAnnotation = declaredField.getAnnotation(ExcelMeta.class);

                    XSSFCell cell = contentRow.createCell(presentAnnotation.index());

                    try {
                        cell.setCellValue(String.valueOf(declaredField.get(content)));
                    } catch (IllegalAccessException e) {
                        log.error("[encountered] [BaseExcelGenerator] : An error has been occurred while add cell value caused by {}", e.getMessage());
                    }
                }
            }
        }
    }

    private void transferTo(XSSFWorkbook workbook) {
        integrityCheck(getSuperPath());

        String destination = dummyFileName();

        try {
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(destination));
            workbook.write(bufferedOutputStream);

            workbook.close();
            bufferedOutputStream.close();

            bufferedOutputStream = null;
        } catch (IOException e) {
            log.error("[encountered] [BaseExcelGenerator] : An error has been occurred while saving file caused by {}", e.getMessage());
        }
    }
}
