package org.palina.reports.service;

public interface ReportsService {

	/**
	 * Extrae el numero de parametros de un query a ejecutar
	 * @param query
	 * @return
	 */
	long readQueryParams(String query);
	
}
