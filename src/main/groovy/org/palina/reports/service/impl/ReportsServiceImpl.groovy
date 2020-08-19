package org.palina.reports.service.impl

import groovy.sql.Sql

import org.palina.reports.dto.ReporteDto
import org.palina.reports.service.ReportsService
import org.springframework.stereotype.Service

import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.streaming.SXSSFSheet
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook

@Service
class ReportsServiceImpl implements ReportsService{

	public void generateReport(ReporteDto request) {
		def paramas = [:]

		request.params.each{ k,v ->
			paramas.put(k.toLowerCase(), new String(v))
		}

		def result = request?.conn.rows(request?.query?.toLowerCase(), paramas)

		if(request?.type.equals("CSV")) 
			generateCSV(request.file, result)
		else if(request?.type.equals("Excel"))
			generateExcel(request.file, result)
	}

	private void generateCSV(String file, def result) {
		def f = new File(file)

		result.each{
			it.each{ k,v ->
				f << v << ","
			}
			f << "\n"
		}
	}

	private void generateExcel(String file, def result) {
		SXSSFWorkbook workBook = new SXSSFWorkbook();
		workBook.setCompressTempFiles(true);

		SXSSFSheet sheet = workBook.createSheet("reporte");
		sheet.setRandomAccessWindowSize(1000);

		int rowNum = 0
		
		result.each{
			Row currentRow = sheet.createRow(rowNum);
			int i = 0
			it.each{ k,v ->
				String val 
				if(v instanceof oracle.sql.BLOB)
					val = new String(((oracle.sql.BLOB)v).getBytes())
				else
					val = v
				currentRow.createCell(i).setCellValue(val);
				i++
			}		
			rowNum++	
		}
		
		FileOutputStream fileOutputStream =  new FileOutputStream(file);
		workBook.write(fileOutputStream);
		fileOutputStream.close();
	}
	
}

