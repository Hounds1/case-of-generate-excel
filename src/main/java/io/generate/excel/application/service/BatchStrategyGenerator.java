package io.generate.excel.application.service;

import io.generate.excel.application.domain.excel.DummyExcelObject;
import io.generate.excel.application.domain.report.MemoryUseCaseReport;
import io.generate.excel.application.utils.dummy.delivery.DummyExcelDeliver;
import io.generate.excel.application.utils.support.annotation.ExcelMeta;
import io.generate.excel.application.utils.support.annotation.IntegrationTestSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Slf4j
public class BatchStrategyGenerator extends AbstractExcelGenerator {

    private final DummyExcelDeliver dummyExcelDeliver;

    @Override
    @IntegrationTestSupport
    public MemoryUseCaseReport generate() {
        MemoryUseCaseReport useCaseReport = new MemoryUseCaseReport();

        List<DummyExcelObject> dummyCells = dummyExcelDeliver.generate(1);

        DummyExcelObject sampleObject = dummyCells.get(0);
        Map<Integer, String> headerCellMap = headerCellMap(sampleObject);

        try (Workbook workBook = new XSSFWorkbook()){
            Sheet sheet = workBook.createSheet("LOCATION_HISTORIES");

            Row headerRow = sheet.createRow(0);
            for (Map.Entry<Integer, String> entry : headerCellMap.entrySet()) {
                Cell cell = headerRow.createCell(entry.getKey());
                cell.setCellValue(entry.getValue());
            }

            int batchSize = 3000;
            int totalSize = dummyCells.size();
            int batches = (int) Math.ceil((double) totalSize / batchSize);
            int pointer = 1;

            for (int i = 0; i < batches; i++) {
                int fromIndex = i * batchSize;
                int toIndex = Math.min(fromIndex + batchSize, totalSize);

                List<DummyExcelObject> batch = dummyCells.subList(fromIndex, toIndex);

                pointer = batch(sheet, batch, pointer);

                transferTo(workBook);
            }
        } catch (IOException e) {
            log.error("[SjgymLocationLogExcelCreator :: generateDailyPresenceLogExcel] : Failed to create workbook or write to file: {}", e.getMessage());
        }

        return useCaseReport;
    }

    private int batch(Sheet sheet, List<DummyExcelObject> chunk, int pointer) {
        AtomicInteger atomicPointer = new AtomicInteger(pointer);

        chunk.forEach(dummyExcel -> {

            Row contentRow = sheet.createRow(atomicPointer.getAndIncrement());

            Arrays.stream(dummyExcel.getClass().getDeclaredFields())
                    .filter(declaredField -> declaredField.isAnnotationPresent(ExcelMeta.class))
                    .forEach(declaredField -> {
                        declaredField.setAccessible(true);
                        ExcelMeta presentAnnotation = declaredField.getAnnotation(ExcelMeta.class);
                        Cell contentCell = contentRow.createCell(presentAnnotation.index());
                        try {
                            String value = String.valueOf(declaredField.get(dummyExcel));
                            contentCell.setCellValue(StringUtils.hasText(value) ? value : "-");
                        } catch (IllegalAccessException e) {
                            contentCell.setCellValue("-");
                        }
                    });
        });

        return atomicPointer.get();
    }

    private void transferTo(Workbook workbook) {
        integrityCheck(getSuperPath());

        String destination = dummyFileName();

        try {
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(destination));
            workbook.write(bufferedOutputStream);

            bufferedOutputStream.close();
        } catch (IOException e) {
            log.error("[encountered] [BaseExcelGenerator] : An error has been occurred while saving file caused by {}", e.getMessage());
        }
    }
}
