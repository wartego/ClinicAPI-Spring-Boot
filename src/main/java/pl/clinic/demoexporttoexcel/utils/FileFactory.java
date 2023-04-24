package pl.clinic.demoexporttoexcel.utils;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.*;

@Component
public class FileFactory {

    public static final String PATH_TEMPLATE = "C:\\Users\\Thanh\\Downloads\\Template\\";

    public static File createExcelFile(String fileName, Workbook workbook) throws Exception {

        workbook = new SXSSFWorkbook();

        OutputStream fileCreated = new FileOutputStream(PATH_TEMPLATE + fileName);

        workbook.write(fileCreated);

        return ResourceUtils.getFile(PATH_TEMPLATE + fileName);
    }




}
