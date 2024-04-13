package io.generate.excel.application.domain.report;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemoryUseCaseReport {

    private Long before;
    private Long after;
    private Long used;

    private void calcUsed() {
        this.used = this.after - this.before;
    }
}
