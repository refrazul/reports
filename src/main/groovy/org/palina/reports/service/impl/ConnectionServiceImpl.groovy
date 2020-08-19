package org.palina.reports.service.impl

import groovy.sql.Sql
import org.jooq.DSLContext
import org.jooq.Parser
import org.jooq.Query
import org.jooq.conf.ParamType
import org.jooq.conf.Settings
import org.jooq.impl.DSL
import org.jooq.impl.DefaultConfiguration
import org.palina.reports.dto.ConnectionDto
import org.palina.reports.service.ConnectioService
import org.springframework.stereotype.Service

import groovy.yaml.YamlSlurper

@Service
class ConnectionServiceImpl implements ConnectioService {

	private def yaml
	
	public ConnectionServiceImpl(String path) {
		def yamlSlurper = new YamlSlurper()
		yaml = yamlSlurper.parseText((path as File).text)
	}
	
	public ConnectionServiceImpl() {
		def yamlSlurper = new YamlSlurper()
		yaml = yamlSlurper.parseText(("/home/refrazul/reports/connections.yml" as File).text)		
	}
	
	public String[] getConnectionsNames() {
		def l1 = ["Seleccionar"]
		def l2 = yaml.connections.list.collect {
			it.name
		}
		
		return (l1+l2) as String[]		
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
						
					def row = sql.firstRow(db.testQuery)
					println "Conexion realizada"
					println row
				}
		
		}catch(Exception e) {			
		}
				
		return sql
	}


	public String getQuery(String path) {
		return new File(path).text		
	}
	
	public List<String> getParams(String queryStr) {
		def qryParamNames=[]
		
		DSLContext ctx = DSL.using(
			new DefaultConfiguration().set(
				new Settings().withParamType(ParamType.NAMED_OR_INLINED)));

		Parser parser = ctx.parser();
		Query query = parser.parseQuery(queryStr);

		query.params.each { k, v ->
			qryParamNames << k
		}
		
		println qryParamNames
		
		return qryParamNames
	}
}
