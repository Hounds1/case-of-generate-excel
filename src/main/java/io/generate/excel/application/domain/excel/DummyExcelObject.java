package io.generate.excel.application.domain.excel;

import io.generate.excel.application.utils.support.annotation.ExcelMeta;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DummyExcelObject {

    @ExcelMeta(index = 0, title = "title1")
    private String sectionFir;
    @ExcelMeta(index = 1, title = "title2")
    private String sectionSec;
    @ExcelMeta(index = 2, title = "title3")
    private String sectionThr;
    @ExcelMeta(index = 3, title = "title4")
    private String sectionFour;
    @ExcelMeta(index = 4, title = "title5")
    private String sectionFive;

    public void initValue() {
        this.sectionFir = "fir";
        this.sectionSec = "sec";
        this.sectionThr = "thr";
        this.sectionFour = "four";
        this.sectionFive = "five";
    }
}
