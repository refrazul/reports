package org.palina.reports.service.impl

import groovy.sql.Sql

import org.palina.reports.dto.ReporteDto
import org.palina.reports.service.ReportsService
import org.springframework.stereotype.Service

@Service
class ReportsServiceImpl implements ReportsService{

	public void generateReport(ReporteDto request) {
		println request.query
		def paramas = [:]
		
		request.params.each{ k,v ->
			paramas.put(k.toLowerCase(), new String(v))
		}
		
		def result = request?.conn.rows(request?.query?.toLowerCase(), paramas)
		
		result.each{
			println it
		}
		
	}
}
