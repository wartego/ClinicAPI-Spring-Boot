package pl.clinic.demoexporttoexcel.utils;

import pl.clinic.demoexporttoexcel.entity.Customer;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.util.List;

import static pl.clinic.demoexporttoexcel.utils.FileFactory.PATH_TEMPLATE;


@Slf4j
@Component
public class ExcelUtils {

    public static ByteArrayInputStream exportCustomer(List<Customer> customers, String fileName) throws Exception {

        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        File file;
        FileInputStream inputStream;
        try {
            file = ResourceUtils.getFile(PATH_TEMPLATE + fileName);
            inputStream = new FileInputStream(file);
        }catch (Exception e){
            log.info("file not found");
            file = FileFactory.createExcelFile(fileName, xssfWorkbook);
            inputStream = new FileInputStream(file);

        }


        XSSFSheet newSheet = xssfWorkbook.createSheet("Sheet1");

        newSheet.createFreezePane(4, 2, 4, 2);

        XSSFFont fontHeader = xssfWorkbook.createFont();
        fontHeader.setFontName("Arial");
        fontHeader.setBold(true);
        fontHeader.setFontHeightInPoints((short) 14);

        XSSFCellStyle titleCellStyle = xssfWorkbook.createCellStyle();
        titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
        titleCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleCellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.index);
        titleCellStyle.setBorderBottom(BorderStyle.MEDIUM);
        titleCellStyle.setBorderTop(BorderStyle.MEDIUM);
        titleCellStyle.setBorderLeft(BorderStyle.MEDIUM);
        titleCellStyle.setBorderRight(BorderStyle.MEDIUM);
        titleCellStyle.setFont(fontHeader);
        titleCellStyle.setWrapText(true);

        XSSFFont font = xssfWorkbook.createFont();
        font.setFontName("Arial");
        font.setBold(false);
        font.setFontHeightInPoints((short) 10);

        XSSFCellStyle cellRowStyle = xssfWorkbook.createCellStyle();
        cellRowStyle.setBorderBottom(BorderStyle.THIN);
        cellRowStyle.setBorderTop(BorderStyle.THIN);
        cellRowStyle.setBorderLeft(BorderStyle.THIN);
        cellRowStyle.setBorderRight(BorderStyle.THIN);
        cellRowStyle.setAlignment(HorizontalAlignment.CENTER);
        cellRowStyle.setWrapText(true);
        cellRowStyle.setFont(font);

        insertColumNameToRow1(ExportConfig.customerExport.getCellExportConfigList(), newSheet, titleCellStyle);
        insertDataToWorkBook(xssfWorkbook, ExportConfig.customerExport, customers, cellRowStyle);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        xssfWorkbook.write(out);
        inputStream.close();
        return new ByteArrayInputStream(out.toByteArray());
    }


    public static <T> void insertDataToWorkBook(Workbook workBook, ExportConfig exportConfig, List<T> datas,
                                                XSSFCellStyle cellStyle) {
        int startRowIdx = exportConfig.getStartRow(); // 2
        int sheetIdx = exportConfig.getSheetIndex(); // 1
        Class clazz = exportConfig.getDataClazz();
        List<CellConfig> cellConfigs = exportConfig.getCellExportConfigList(); // 1
        Sheet sheet = workBook.getSheetAt(sheetIdx);
        int currentRowIdx = startRowIdx;
        for (T data : datas) {

            Row currentRow = sheet.getRow(currentRowIdx);
            if (currentRow == null) {
                currentRow = sheet.createRow(currentRowIdx);
            }
            // export data to row
            insertDataToRow(data, currentRow, cellConfigs, clazz, sheet, cellStyle);
            // increase currentRowIndex
            currentRowIdx++;
        }
    }

    private static <T> void insertColumNameToRow1(List<CellConfig> cellConfigs,
                                                  Sheet sheet, XSSFCellStyle titleCellStyle) {
        int currentRow = sheet.getTopRow();
        Row row = sheet.createRow(currentRow);
        int i = 0;
        sheet.autoSizeColumn(currentRow);
        for (CellConfig cellConfig : cellConfigs) {
            Cell currentCell = row.createCell(i);
            String columnName = cellConfig.getFieldName();
            currentCell.setCellValue(columnName);
            currentCell.setCellStyle(titleCellStyle);
            sheet.autoSizeColumn(i);
            i++;
        }
    }

    private static <T> void insertDataToRow(T data, Row currentRow, List<CellConfig> cellConfigs, Class clazz,
                                            Sheet sheet, XSSFCellStyle cellStyle) {
        for (CellConfig cellConfig : cellConfigs) {
            Cell currentCell = currentRow.getCell(cellConfig.getColIndex());
            if (currentCell == null) {
                currentCell = currentRow.createCell(cellConfig.getColIndex());
            }
            // get data for cell
            String cellValue = getCellValue(data, cellConfig, clazz);
            // set data
            if (!ObjectUtils.isEmpty(cellValue)) {
                currentCell.setCellValue(cellValue);
            } else {
                currentCell.setCellValue("");
            }
            sheet.autoSizeColumn(cellConfig.getColIndex());
            currentCell.setCellStyle(cellStyle);
        }
    }

    private static <T> String getCellValue(T data, CellConfig cellConfig, Class clazz) {
        String fieldName = cellConfig.getFieldName();
        try {
//           return  FieldUtils.readField(data, field);
            Field field = getDeclareField(clazz, fieldName);
            if (field != null) {
                field.setAccessible(true);
                return field.get(data) != null ? field.get(data).toString() : "";
            }
            return "";
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "";
        }
    }

    private static Field getDeclareField(Class clazz, String fieldName) {
        if (clazz == null || fieldName.isEmpty()) {
            return null;
        }
        do {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        } while ((clazz = clazz.getSuperclass()) != null);

        return null;
    }


}
