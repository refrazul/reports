package org.palina.reports.service;

import org.palina.reports.dto.ReporteDto;
import org.palina.reports.enums.ResponseReportEnum;


public interface ReportsService {
	
	ResponseReportEnum generateReport(ReporteDto request);
}
