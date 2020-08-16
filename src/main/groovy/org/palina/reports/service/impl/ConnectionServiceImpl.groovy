package org.palina.reports.service.impl

import groovy.sql.Sql

import org.palina.reports.dto.ConnectionDto
import org.palina.reports.service.ConnectioService
import org.springframework.stereotype.Service

import groovy.yaml.YamlSlurper

@Service
class ConnectionServiceImpl implements ConnectioService {

	private def yaml
	
	public ConnectionServiceImpl() {
		def yamlSlurper = new YamlSlurper()
		yaml = yamlSlurper.parseText(("/home/refrazul/reports/connections.yml" as File).text)		
	}
	
	
	public Sql getConnection(String connectionName) {
		def db 
		def sql 
			
		try {
			yaml.connections.list.each{
				
					if(it.name.equals(connectionName)) {
						db = new ConnectionDto(url: it.datasource.url, user: it.datasource.username, password: it.datasource.password, driver: it.datasource.driverClassName, testQuery: it.datasource.testQuery)
						return
					}
				}
						
				if(db) {
					sql = Sql.newInstance(db.url, db.user, db.password, db.driver)
					println db.testQuery
						
					def row = sql.firstRow(db.testQuery)
					println "Conexion realizada"
					println row
				}
		
		}catch(Exception e) {			
		}
				
		return sql
	}
}
