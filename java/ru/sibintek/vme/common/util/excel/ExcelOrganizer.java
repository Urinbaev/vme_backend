package ru.sibintek.vme.common.util.excel;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.util.CollectionUtils;
import ru.sibintek.vme.common.constant.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class ExcelOrganizer {
    private static final String FONT_FAMILY = "Times New Roman";
    private static final int HEADER_FONT_SIZE = 12;
    private static final int CONTENT_FONT_SIZE = 10;

    public static void writeToResponse(
            String title,
            Map<String, String> fieldsSet,
            List<Map<String, Object>> records,
            HttpServletResponse response
    ) throws ExcelOrganizerException {
        try {
            try (var wb = new XSSFWorkbook()) {
                var sheet = wb.createSheet(title);
                writeHeader(fieldsSet, sheet, wb);
                write(fieldsSet, records, sheet, wb);

                try (var outputStream = response.getOutputStream()) {
                    response.setContentType(MediaType.APPLICATION_XML_TYPE);
                    response.setHeader(
                            HttpHeaders.CONTENT_DISPOSITION,
                            String.format(
                                    "attachment;filename=export_%s.xlsx",
                                    LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE)
                            )
                    );

                    wb.write(outputStream);
                }
            }
        } catch (IOException e) {
            throw new ExcelOrganizerException(
                    MessageFormat.format(
                            "Возникла ошибка во время обработки данных для записи в модель ответа. {0}",
                            e.getMessage()
                    )
            );
        }
    }

    private static void writeHeader(Map<String, String> fieldsSet, XSSFSheet sheet, XSSFWorkbook wb) {
        var style = wb.createCellStyle();
        var font = wb.createFont();
        font.setBold(true);
        font.setFontHeight(HEADER_FONT_SIZE);
        font.setFontName(FONT_FAMILY);
        style.setFont(font);

        var row = sheet.createRow(0);
        var headers = fieldsSet.keySet().toArray();
        for (var i = 0; i < headers.length; i++) {
            createCell(row, i, headers[i], style, sheet);
        }
    }

    private static void write(
            Map<String, String> fieldsSet,
            List<Map<String, Object>> records,
            XSSFSheet sheet,
            XSSFWorkbook wb
    ) {
        if (CollectionUtils.isEmpty(records)) {
            return;
        }

        var style = wb.createCellStyle();
        var font = wb.createFont();
        font.setFontHeight(CONTENT_FONT_SIZE);
        font.setFontName(FONT_FAMILY);
        style.setFont(font);

        int rowCount = 1;
        for (var record : records) {
            int columnCount = 0;
            var row = sheet.createRow(rowCount++);
            for (var field : fieldsSet.entrySet()) {
                createCell(row, columnCount++, record.get(field.getValue()), style, sheet);
            }
        }
    }

    private static void createCell(Row row, int columnCount, Object value, CellStyle style, XSSFSheet sheet) {
        sheet.autoSizeColumn(columnCount);

        var cell = row.createCell(columnCount);
        cell.setCellStyle(style);

        if (value == null) {
            return;
        }

        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue((String) value);
        }
    }
}
