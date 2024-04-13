package io.generate.excel.application.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ForcedDismissGeneratorTest {

    @Autowired
    private ForcedDismissGenerator forcedDismissGenerator;

    @Test
    void generate() {
        forcedDismissGenerator.generate();
    }
}