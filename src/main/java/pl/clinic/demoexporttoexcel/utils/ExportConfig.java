package pl.clinic.demoexporttoexcel.utils;

import pl.clinic.demoexporttoexcel.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExportConfig {

    private int sheetIndex;
    private int startRow;
    private Class dataClazz;
    private List<CellConfig> cellExportConfigList;


    public static final ExportConfig customerExport;
    static {
        customerExport = new ExportConfig();
        customerExport.setSheetIndex(0);
        customerExport.setStartRow(1);
        customerExport.setDataClazz(Customer.class);
        List<CellConfig> cellConfigList = new ArrayList<>();
        cellConfigList.add(new CellConfig(0, "customerNumber"));
        cellConfigList.add(new CellConfig(1, "customerName"));
        cellConfigList.add(new CellConfig(2, "contactLastName"));
        cellConfigList.add(new CellConfig(3, "contactFirstName"));
        cellConfigList.add(new CellConfig(4, "phone"));
        cellConfigList.add(new CellConfig(5, "addressLine1"));
        cellConfigList.add(new CellConfig(6, "addressLine2"));
        cellConfigList.add(new CellConfig(7, "city"));
        cellConfigList.add(new CellConfig(8, "state"));
        cellConfigList.add(new CellConfig(9, "postalCode"));
        cellConfigList.add(new CellConfig(10, "country"));
        cellConfigList.add(new CellConfig(11, "salesRepEmployeeNumber"));
        cellConfigList.add(new CellConfig(12, "creditLimit"));
        customerExport.setCellExportConfigList(cellConfigList);
    }
}
