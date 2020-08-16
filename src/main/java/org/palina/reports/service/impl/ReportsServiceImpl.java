package org.palina.reports.service.impl;

import java.sql.Connection;

import org.palina.reports.service.ReportsService;
import org.springframework.stereotype.Service;

@Service
public class ReportsServiceImpl implements ReportsService {

	void executeReport(Connection conn, String query) {
		query = "select * from istcard where pan = ? and statsu = ?";
		
		readQueryParams(query);
	}
	
	
	public  long readQueryParams(String query) {
		query = "select * from istcard where pan = ? and statsu = ?";
		
		long params = query.chars().filter(ch -> ch == '?').count();
	
		return params;
	}
	
}
