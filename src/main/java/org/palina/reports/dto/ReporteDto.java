package org.palina.reports.dto;

import java.util.Map;

import groovy.sql.Sql;

public class ReporteDto {

	private String  query;
	private Sql conn;
	private Map<String,Object> params;
		

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
	
}