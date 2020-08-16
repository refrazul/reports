package org.palina.reports.dto;

import groovy.sql.Sql;

public class ReporteDto {

	private String  ruta;
	private Sql conn;
		
	public String getRuta() {
		return ruta;
	}
	public void setRuta(String ruta) {
		this.ruta = ruta;
	}
	public Sql getConn() {
		return conn;
	}
	public void setConn(Sql conn) {
		this.conn = conn;
	}
}