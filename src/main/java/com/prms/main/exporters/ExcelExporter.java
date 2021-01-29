package com.prms.main.exporters;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.prms.main.models.Address;
import com.prms.main.models.Patient;

public class ExcelExporter{
	private XSSFWorkbook wb;
	private XSSFSheet ws;

	private List<Patient> records;
	private List<Address> addresses;

	public ExcelExporter(List<Patient> records, List<Address> addresses){
		this.records = records;
		this.addresses = addresses;
		wb = new XSSFWorkbook();
		ws = wb.createSheet("Patient Records");
	}

	private void writeHeader(){
		CellStyle s = wb.createCellStyle();
		XSSFFont f = wb.createFont();
		f.setBold(true);
		f.setFontHeight(12);
		s.setFont(f);
		s.setAlignment(HorizontalAlignment.CENTER);

		Row r = ws.createRow(0);
		createCell(r, 0, "ID", s);   
		createCell(r, 1, "STATUS", s);   
		createCell(r, 2, "LAST_NAME", s);   
		createCell(r, 3, "FIRST_NAME", s);   
		createCell(r, 4, "MIDDLE_NAME", s);   
		createCell(r, 5, "BIRTHDATE", s);   
		createCell(r, 6, "GENDER", s);   
		createCell(r, 7, "EMAIL_ADDRESS", s);
		createCell(r, 8, "CONTACT_NUMBER", s); 
		createCell(r, 9, "PRIMARY_ADDRESS", s); 
	}

	private void writeData(){
		int rCount = 1;
		
		XSSFFont f = wb.createFont();
		f.setFontHeight(11);
		
		CellStyle s1 = wb.createCellStyle();
		s1.setFont(f);
		s1.setAlignment(HorizontalAlignment.LEFT);
		
		CellStyle s2 = wb.createCellStyle();
		s2.setFont(f);
		s2.setAlignment(HorizontalAlignment.CENTER);
		

		for(Patient p : records){
			
			Row r = ws.createRow(rCount);
		
			createCell(r, 0, p.getPatientId(), s1);   
			createCell(r, 1, p.getStatus()==1 ? "active":"inactive", s1);   
			createCell(r, 2, p.getLastName(), s1);   
			createCell(r, 3, p.getFirstName(), s1);   
			createCell(r, 4, p.getMiddleName(), s1);   
			createCell(r, 5, p.getBirthdate(), s1);   
			createCell(r, 6, p.getGender(), s2);  
			createCell(r, 7, p.getEmail(), s1);
			createCell(r, 8, p.getContactNumber(), s2);    
			createCell(r, 9, p.getAddress(), s1);    
			
			int columnNo = 10;
			int aCounter = 1;
			for (Address a : addresses){
				
				if (a.getPatientId() == p.getPatientId()){
					Cell cell = ws.getRow(0).createCell(columnNo);

					if(cell.getStringCellValue() == ""){ 
						XSSFFont f2 = wb.createFont();
						f2.setBold(true);
						f2.setFontHeight(12);
						
						CellStyle s3 = wb.createCellStyle();
						s3.setFont(f2);
						s3.setAlignment(HorizontalAlignment.CENTER);
						
						cell.setCellValue("OTHER_ADDRESS_" + aCounter);
						cell.setCellStyle(s3);
						
						aCounter++;
					}
					
					cell = ws.getRow(rCount).createCell(columnNo);					
					cell.setCellValue(a.getAddress());
					cell.setCellStyle(s1);
					ws.autoSizeColumn(columnNo);
					columnNo++;
				}	
			}
			rCount++;  
		}
	}

	private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        Cell cell = row.createCell(columnCount);
        
        if (value instanceof Date) {
    		XSSFFont f = wb.createFont();
    		f.setFontHeight(11);
        	CellStyle s = wb.createCellStyle();
        	XSSFCreationHelper createHelper = wb.getCreationHelper();
    		s.setFont(f);
			s.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));
			s.setAlignment(HorizontalAlignment.CENTER);
			cell.setCellValue((Date) value);
			cell.setCellStyle(s);
	        ws.autoSizeColumn(columnCount);
        } else {
			if (value instanceof Integer) {
				System.out.println(value);
			    cell.setCellValue((Integer) value);
			} else if (value instanceof Boolean) {
			    cell.setCellValue((Boolean) value);
			} else if (value instanceof Long){
				XSSFCreationHelper createHelper = wb.getCreationHelper();
				style.setDataFormat(createHelper.createDataFormat().getFormat(""));
				cell.setCellValue((Long) value);
			} else {
				cell.setCellValue((String) value);
			}	
	        cell.setCellStyle(style);
	        ws.autoSizeColumn(columnCount);
        }
        
    }
	
	public ByteArrayOutputStream export() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		writeHeader();
		writeData();

		wb.write(out);
		return out;
	}
}