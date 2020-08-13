package org.palina.reports.service

import java.sql.Connection
import org.jooq.DSLContext
import org.jooq.Parser
import org.jooq.Query
import org.jooq.conf.ParamType
import org.jooq.conf.Settings
import org.jooq.impl.DSL
import org.jooq.impl.DefaultConfiguration
import org.springframework.stereotype.Service

@Service
class ReportsService {
	
	
	def executeReport(Connection conn, String query) {
		parseQuery()
	}
	
	
	private def parseQuery() {
		
		DSLContext ctx = DSL.using(
			new DefaultConfiguration().set(
				new Settings().withParamType(ParamType.FORCE_INDEXED)));

		Parser parser = ctx.parser();
		Query query = parser.parseQuery(
			"select * "
		  + "from tableName as t1 "
		  + "inner join tableName2 as t2 "
		  + "on t1.tableColumnId=t2.tableColumnId "
		  + "where t1.tableColumnId=4 and t1.tableColumnName='test'");

		System.out.println(ctx
			.renderContext()
			.paramType(ParamType.FORCE_INDEXED)
			.visit(query)
			.render());
		
		
	}
	
}
