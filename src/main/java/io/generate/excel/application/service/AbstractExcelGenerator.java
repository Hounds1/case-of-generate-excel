package io.generate.excel.application.service;

import io.generate.excel.application.domain.excel.DummyExcelObject;
import io.generate.excel.application.domain.report.MemoryUseCaseReport;
import io.generate.excel.application.utils.support.annotation.ExcelMeta;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractExcelGenerator {

    @Value("${expression.file.base.path}")
    private String BASE_PATH;

    public abstract MemoryUseCaseReport generate();

    Map<Integer, String> headerCellMap(DummyExcelObject sampleObject) {
        Map<Integer, String> headerCellMap = new LinkedHashMap<>();

        Field[] declaredFields = sampleObject.getClass().getDeclaredFields();

        for (Field declaredField : declaredFields) {
            if (!declaredField.canAccess(sampleObject)) {
                declaredField.setAccessible(true);
            }

            if (declaredField.isAnnotationPresent(ExcelMeta.class)) {
                ExcelMeta presentAnnotation = declaredField.getAnnotation(ExcelMeta.class);

                headerCellMap.put(presentAnnotation.index(), presentAnnotation.title());
            }
        }

        return headerCellMap;
    }

    boolean integrityCheck(String path) {
        File directory = new File(path);

        if (!directory.exists()) {
            return directory.mkdir();
        } else {
            return true;
        }
    }

    String dummyFileName() {
        return this.BASE_PATH + "/" + "dummy.xlsx";
    }

    String getSuperPath() {
        return this.BASE_PATH;
    }
}
