package org.palina.reports.dto;

import java.util.Map;

import groovy.sql.Sql;

/**
 * Contiene los parametros para un reporte
 * @author refrazul
 *
 */
public class ReporteDto {

	private String  query;
	private Sql conn;
	private Map<String,Object> params;
	private String type;
	private String file;

	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public Sql getConn() {
		return conn;
	}
	public void setConn(Sql conn) {
		this.conn = conn;
	}
	public Map<String, Object> getParams() {
		return params;
	}
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	@Override
	public String toString() {
		return "ReporteDto [query=" + query + ", conn=" + conn + ", params=" + params + ", type=" + type + ", file="
				+ file + "]";
	}
	
}