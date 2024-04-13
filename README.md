# Case of generated Excel
- 각 방식에 따른 엑셀 파일 생성 간 발생하는 메모리 부하를 확인해봅시다.

# Cases
> [#1] BaseExcelGenerator [[링크]](https://github.com/Hounds1/case-of-generate-excel/blob/main/src/main/java/io/generate/excel/application/service/BaseExcelGenerator.java) \
[#2] ChangeOutputStreamGenerator [[링크]](https://github.com/Hounds1/case-of-generate-excel/blob/main/src/main/java/io/generate/excel/application/service/ChangeOutputStreamGenerator.java) \
[#3] ForcedDismissGenerator [[링크]](https://github.com/Hounds1/case-of-generate-excel/blob/main/src/main/java/io/generate/excel/application/service/ChangeOutputStreamGenerator.java) \
[#4] BaseWithFoecedDismissGenerator [[링크]](https://github.com/Hounds1/case-of-generate-excel/blob/main/src/main/java/io/generate/excel/application/service/ChangeOutputStreamGenerator.java)


# Support
> [#1] IntegrationTestSupportAspect [[링크]](https://github.com/Hounds1/case-of-generate-excel/blob/main/src/main/java/io/generate/excel/application/utils/support/aspect/IntegrationTestSupportAspect.java)
> > 호출 별 진입 전, 후 메모리 사용량 기록
