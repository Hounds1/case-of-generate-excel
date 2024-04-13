package io.generate.excel.application.utils.dummy.delivery;

import io.generate.excel.application.domain.excel.DummyExcelObject;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DummyExcelDeliver {

    public List<DummyExcelObject> generate(int range) {
        int round = range * 10000;

        List<DummyExcelObject> response = new ArrayList<>();

        for (int i = 0; i < round; i++) {
            DummyExcelObject dummyExcelObject = new DummyExcelObject();
            dummyExcelObject.initValue();

            response.add(dummyExcelObject);
        }

        return response;
    }
}
