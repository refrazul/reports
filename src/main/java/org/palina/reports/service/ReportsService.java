package org.palina.reports.service;

import org.palina.reports.dto.ReporteDto;
import org.palina.reports.enums.ResponseReportEnum;


public interface ReportsService {
	
	/**
	 * Genera un reporte a partir de una consulta sql
	 * @param request Objeto con datos para generación de un reporte
	 * @return ResponseReportEnum 
	 */
	ResponseReportEnum generateReport(ReporteDto request);
}
