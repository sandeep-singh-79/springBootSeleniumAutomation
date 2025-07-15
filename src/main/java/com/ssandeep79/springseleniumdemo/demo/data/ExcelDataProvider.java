package com.ssandeep79.springseleniumdemo.demo.data;

import com.ssandeep79.springseleniumdemo.demo.exception.FrameworkException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Excel data provider for loading test data from Excel files
 * Supports reading and processing test data in XLSX format
 */
@Component
public class ExcelDataProvider {
    private static final Logger logger = LoggerFactory.getLogger(ExcelDataProvider.class);
    
    /**
     * Gets test data from Excel file as Object array for TestNG data provider
     * @param filePath path to the Excel file (in resources folder)
     * @param sheetName name of the sheet to read
     * @param filterColumn optional filter column name (null for no filtering)
     * @param filterValue optional filter value (null for no filtering)
     * @return Object array suitable for TestNG data provider
     */
    public Object[][] getTestData(String filePath, String sheetName, String filterColumn, String filterValue) {
        List<Map<String, String>> data = readExcelData(filePath, sheetName, filterColumn, filterValue);
        
        Object[][] testData = new Object[data.size()][1];
        for (int i = 0; i < data.size(); i++) {
            testData[i][0] = data.get(i);
        }
        
        return testData;
    }
    
    /**
     * Reads data from Excel file and returns as list of row data maps
     * @param filePath path to the Excel file (in resources folder)
     * @param sheetName name of the sheet to read
     * @param filterColumn optional filter column name (null for no filtering)
     * @param filterValue optional filter value (null for no filtering)
     * @return List of maps containing row data (column name -> cell value)
     */
    public List<Map<String, String>> readExcelData(String filePath, String sheetName, String filterColumn, String filterValue) {
        List<Map<String, String>> excelData = new ArrayList<>();
        
        try {
            Resource resource = new ClassPathResource(filePath);
            try (InputStream is = resource.getInputStream();
                 Workbook workbook = new XSSFWorkbook(is)) {
                
                Sheet sheet = workbook.getSheet(sheetName);
                if (sheet == null) {
                    throw FrameworkException.dataError("Sheet '" + sheetName + "' not found in file: " + filePath);
                }
                
                // Get header row
                Row headerRow = sheet.getRow(0);
                if (headerRow == null) {
                    throw FrameworkException.dataError("Header row not found in sheet: " + sheetName);
                }
                
                // Extract header column names
                List<String> headers = new ArrayList<>();
                for (Cell cell : headerRow) {
                    headers.add(getCellValueAsString(cell));
                }
                
                // Find filter column index if needed
                int filterColumnIndex = -1;
                if (filterColumn != null) {
                    for (int i = 0; i < headers.size(); i++) {
                        if (headers.get(i).equalsIgnoreCase(filterColumn)) {
                            filterColumnIndex = i;
                            break;
                        }
                    }
                    if (filterColumnIndex == -1) {
                        logger.warn("Filter column '{}' not found in headers", filterColumn);
                    }
                }
                
                // Process data rows
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) continue;
                    
                    // Skip rows that don't match the filter criteria
                    if (filterColumnIndex >= 0 && filterValue != null) {
                        Cell filterCell = row.getCell(filterColumnIndex);
                        String filterCellValue = getCellValueAsString(filterCell);
                        if (!filterValue.equalsIgnoreCase(filterCellValue)) {
                            continue;
                        }
                    }
                    
                    // Extract row data
                    Map<String, String> rowData = new HashMap<>();
                    for (int j = 0; j < headers.size(); j++) {
                        Cell cell = row.getCell(j);
                        String value = getCellValueAsString(cell);
                        rowData.put(headers.get(j), value);
                    }
                    
                    excelData.add(rowData);
                }
                
            }
        } catch (IOException e) {
            throw FrameworkException.dataError("Error reading Excel file: " + filePath + " - " + e.getMessage());
        }
        
        return excelData;
    }
    
    /**
     * Converts cell value to String regardless of the cell type
     * @param cell the Excel cell
     * @return string representation of the cell value
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toString();
                }
                // Convert to string but remove decimal point for whole numbers
                double value = cell.getNumericCellValue();
                if (value == (long) value) {
                    return String.format("%d", (long) value);
                }
                return String.valueOf(value);
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return String.valueOf(cell.getNumericCellValue());
                } catch (IllegalStateException e) {
                    try {
                        return cell.getStringCellValue();
                    } catch (IllegalStateException e2) {
                        return cell.getCellFormula();
                    }
                }
            case BLANK:
                return "";
            default:
                return "";
        }
    }
    
    /**
     * Gets all sheet names from an Excel file
     * @param filePath path to the Excel file (in resources folder)
     * @return list of sheet names
     */
    public List<String> getSheetNames(String filePath) {
        List<String> sheetNames = new ArrayList<>();
        
        try {
            Resource resource = new ClassPathResource(filePath);
            try (InputStream is = resource.getInputStream();
                 Workbook workbook = new XSSFWorkbook(is)) {
                
                int numberOfSheets = workbook.getNumberOfSheets();
                for (int i = 0; i < numberOfSheets; i++) {
                    sheetNames.add(workbook.getSheetName(i));
                }
            }
        } catch (IOException e) {
            throw FrameworkException.dataError("Error reading Excel file: " + filePath + " - " + e.getMessage());
        }
        
        return sheetNames;
    }
    
    /**
     * Writes data to Excel file (creates new file or overwrites existing)
     * @param filePath path to the Excel file to create/update
     * @param sheetName name of the sheet to write to
     * @param headers column headers
     * @param data list of data rows (each row is a map of column name -> value)
     */
    public void writeExcelData(String filePath, String sheetName, 
                               List<String> headers, List<Map<String, String>> data) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(sheetName);
            
            // Create cell style for header row
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers.get(i));
                cell.setCellStyle(headerStyle);
            }
            
            // Create data rows
            for (int i = 0; i < data.size(); i++) {
                Row row = sheet.createRow(i + 1);
                Map<String, String> rowData = data.get(i);
                
                for (int j = 0; j < headers.size(); j++) {
                    String header = headers.get(j);
                    String value = rowData.getOrDefault(header, "");
                    row.createCell(j).setCellValue(value);
                }
            }
            
            // Auto-size columns
            for (int i = 0; i < headers.size(); i++) {
                sheet.autoSizeColumn(i);
            }
            
            // Write to file
            java.nio.file.Path path = java.nio.file.Paths.get(filePath);
            java.nio.file.Files.createDirectories(path.getParent());
            
            try (java.io.OutputStream fileOut = java.nio.file.Files.newOutputStream(path)) {
                workbook.write(fileOut);
            }
            
            logger.info("Excel file written successfully: {}", filePath);
            
        } catch (IOException e) {
            throw FrameworkException.dataError("Error writing Excel file: " + filePath + " - " + e.getMessage());
        }
    }
}
