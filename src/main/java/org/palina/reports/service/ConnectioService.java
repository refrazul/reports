package org.palina.reports.service;

import java.util.List;

import groovy.sql.Sql;

/**
 * Manejo de conexiones
 * @author refrazul
 *
 */
public interface ConnectioService {
	
	/**
	 * Genera un objeto para realizar consulta a base de datos
	 * @param connectionName Nombre de la conexión
	 * @return Objeto para hacer conexiones
	 */
	Sql getConnection(String connectionName);
	
	/**
	 * Extrae la consulta de un archivo
	 * @param path Ruta del archivo
	 * @return Cadena con la consulta
	 */
	String getQuery(String path);
	
	/**
	 * Extrae los nombres de los parametros de una consulta
	 * @param sql Cadena con la consulta
	 * @return Listado con los nombres de parametros
	 */
	List<String> getParams(String sql);
	
	/**
	 * Extrae los nombres de conexiones de un archivo yml
	 * @return Array con los nombres de conexiones
	 */
	String[] getConnectionsNames(); 
}
