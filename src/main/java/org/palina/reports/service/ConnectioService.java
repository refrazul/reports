package org.palina.reports.service;


import java.util.List;
import java.util.Map;

import groovy.sql.Sql;

public interface ConnectioService {

	Sql getConnection(String connectionName);
	
	String getQuery(String path);
	
	List<String> getParams(String sql);
}
