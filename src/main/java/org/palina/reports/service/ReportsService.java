package org.palina.reports.service;

import org.palina.reports.dto.ReporteDto;


public interface ReportsService {
	
	void generateReport(ReporteDto request);
}
