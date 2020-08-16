package org.palina.reports.service;

import groovy.sql.Sql;

public interface ConnectioService {

	Sql getConnection(String connectionName);
	
}
