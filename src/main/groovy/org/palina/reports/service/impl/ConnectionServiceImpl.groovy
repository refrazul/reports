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
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

import groovy.yaml.YamlSlurper

@Service
class ConnectionServiceImpl implements ConnectioService {

	Logger logger = LoggerFactory.getLogger(ConnectionServiceImpl.class);
	private def yaml
	
	
	public ConnectionServiceImpl(String path) {
		def yamlSlurper = new YamlSlurper()
		yaml = yamlSlurper.parseText((path as File).text)
	}
	
	
	public ConnectionServiceImpl() {
		def yamlSlurper = new YamlSlurper()
		yaml = yamlSlurper.parseText(("/home/refrazul/reports/connections.yml" as File).text)		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getConnectionsNames() {
		def l1 = ["Seleccionar"]
		def l2 = yaml.connections.list.collect {
			it.name
		}
		
		return (l1+l2) as String[]		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
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
					logger.info( "Conexion realizada" )
					logger.info( row )
				}
		
		}catch(Exception e) {			
		}
				
		return sql
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getQuery(String path) {
		return new File(path).text		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
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
		
		logger.info( qryParamNames )
		
		return qryParamNames
	}
}
