package org.palina.reports.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.palina.reports.dto.ReporteDto;
import org.palina.reports.enums.ResponseReportEnum;
import org.palina.reports.service.impl.ConnectionServiceImpl;
import org.palina.reports.service.impl.ReportsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringRunner;

import groovy.sql.Sql;

@RunWith(SpringRunner.class)
public class ReportsServiceTest {

    Logger logger = LoggerFactory.getLogger(ReportsServiceTest.class);
	
	private ReportsService reportsService = null;
	private ConnectioService connectioService = null;
	
	@Before
	public void setUp() throws Exception {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("connections.yml").getFile());
		
		connectioService = new ConnectionServiceImpl(file.getAbsolutePath());
		reportsService = new ReportsServiceImpl();
	}
	
	@After
	public void shutDown() {
		logger.debug("Borrando archivos generados");
		File file = new File("salida.csv");
		file.delete();
		file = new File("salida.xlsx");
		file.delete();
	}
	
	@Test
	public void generaReporteCVS()  {
		logger.debug("Ejecutando servicio" +  reportsService);
		Sql conn = connectioService.getConnection("DB Test");
		
		ReporteDto request = new ReporteDto();
		request.setConn(conn);
		request.setQuery("select * from USER_APP");
		request.setType("CSV");
		request.setFile("salida.csv");
		request.setParams(new HashMap<String,Object>());		
		ResponseReportEnum res = reportsService.generateReport(request);
		conn.close();
		
		assertEquals(res, ResponseReportEnum.REPORTE_GENRADO);
	}

	@Test
	public void generaReporteExcel()  {
		logger.debug("Ejecutando servicio" +  reportsService);
		Sql conn = connectioService.getConnection("DB Test");
		
		ReporteDto request = new ReporteDto();
		request.setConn(conn);
		request.setQuery("select * from USER_APP");
		request.setType("Excel");
		request.setFile("salida.xlsx");
		request.setParams(new HashMap<String,Object>());		
		ResponseReportEnum res = reportsService.generateReport(request);
		conn.close();
		
		assertEquals(res, ResponseReportEnum.REPORTE_GENRADO);
	}

	@Test
	public void generaReporteErroConsulta()  {
		logger.debug("Ejecutando servicio" +  reportsService);
		Sql conn = connectioService.getConnection("DB Test");
		
		ReporteDto request = new ReporteDto();
		request.setConn(conn);
		request.setQuery("select * from USER_APP2");
		request.setType("Excel");
		request.setFile("salida.xlsx");
		request.setParams(new HashMap<String,Object>());		
		ResponseReportEnum res = reportsService.generateReport(request);
		conn.close();
		
		assertEquals(res, ResponseReportEnum.ERROR_AL_EJECUTAR_CONSULTA);
	}
	
	@Test
	public void generaReporteNoHayRegistros()  {
		logger.debug("Ejecutando servicio" +  reportsService);
		Sql conn = connectioService.getConnection("DB Test");
		
		ReporteDto request = new ReporteDto();
		request.setConn(conn);
		request.setQuery("select * from USER_APP where gender = :gender");
		request.setType("Excel");
		request.setFile("salida.xlsx");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("gender", "Other");
		request.setParams(params);		
		ResponseReportEnum res = reportsService.generateReport(request);
		conn.close();
		
		assertEquals(res, ResponseReportEnum.NO_SE_ENCONTRARON_REGISTROS);
	}
	
	
}
