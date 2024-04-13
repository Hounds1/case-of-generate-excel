package io.generate.excel.application.controller;

import io.generate.excel.application.domain.report.MemoryUseCaseReport;
import io.generate.excel.application.service.BaseExcelGenerator;
import io.generate.excel.application.service.BaseWithForcedDismissGenerator;
import io.generate.excel.application.service.ChangeOutputStreamGenerator;
import io.generate.excel.application.service.ForcedDismissGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/test")
public class GeneratorUnionController {

    private final BaseExcelGenerator baseExcelGenerator;
    private final ChangeOutputStreamGenerator changeOutputStreamGenerator;
    private final ForcedDismissGenerator forcedDismissGenerator;
    private final BaseWithForcedDismissGenerator baseWithForcedDismissGenerator;

    @GetMapping("/generate/type/base")
    public ResponseEntity<MemoryUseCaseReport> generateBaseType() {
        MemoryUseCaseReport report = baseExcelGenerator.generate();

        return ResponseEntity.status(HttpStatus.OK).body(report);
    }

    @GetMapping("/generate/type/change")
    public ResponseEntity<MemoryUseCaseReport> generateChangeType() {
        MemoryUseCaseReport report = changeOutputStreamGenerator.generate();

        return ResponseEntity.status(HttpStatus.OK).body(report);
    }

    @GetMapping("/generate/type/dismiss")
    public ResponseEntity<MemoryUseCaseReport> generateDismissType() {
        MemoryUseCaseReport report = forcedDismissGenerator.generate();

        return ResponseEntity.status(HttpStatus.OK).body(report);
    }

    @GetMapping("/generate/type/sub/dismiss")
    public ResponseEntity<MemoryUseCaseReport> generateSubDismissType() {
        MemoryUseCaseReport report = baseWithForcedDismissGenerator.generate();

        return ResponseEntity.status(HttpStatus.OK).body(report);
    }
}
