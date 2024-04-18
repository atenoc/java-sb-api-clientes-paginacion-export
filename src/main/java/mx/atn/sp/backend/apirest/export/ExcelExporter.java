package mx.atn.sp.backend.apirest.export;

import org.springframework.stereotype.Component;

import mx.atn.sp.backend.apirest.models.entity.Cliente;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Component
public class ExcelExporter {
	
	/* Método que sólo Genera el archivo Excel y lo Devuelve*/
	public Workbook generarExcel(List<Cliente> clientes) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Clientes");

        // Crear encabezados de columnas
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Nombre");
        headerRow.createCell(2).setCellValue("Apellido");
        headerRow.createCell(3).setCellValue("Email");
        headerRow.createCell(4).setCellValue("Fecha de Creación");

        // Rellenar los datos de los clientes en filas
        int rowNum = 1;
        for (Cliente cliente : clientes) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(cliente.getId());
            row.createCell(1).setCellValue(cliente.getNombre());
            row.createCell(2).setCellValue(cliente.getApellido());
            row.createCell(3).setCellValue(cliente.getEmail());
            row.createCell(4).setCellValue(cliente.getCreateAt().toString());
        }

        // Ajustar el tamaño de las columnas
        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }

        return workbook;
    }

	/* Método para exportar el Excel un directorio Local*/
	public void exportToExcel(List<Cliente> clientes, String filePath) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Clientes");

        // Crear encabezados de columna
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Nombre");
        headerRow.createCell(2).setCellValue("Apellido");
        headerRow.createCell(3).setCellValue("Email");
        headerRow.createCell(4).setCellValue("Fecha de Creación");

        // Llenar filas con datos de clientes
        int rowNum = 1;
        for (Cliente cliente : clientes) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(cliente.getId());
            row.createCell(1).setCellValue(cliente.getNombre());
            row.createCell(2).setCellValue(cliente.getApellido());
            row.createCell(3).setCellValue(cliente.getEmail());
            row.createCell(4).setCellValue(cliente.getCreateAt().toString());
        }

        // Guardar el archivo Excel
        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            workbook.write(outputStream);
        }
    }
}
