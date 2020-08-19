package org.palina.reports.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.palina.reports.service.impl.ConnectionServiceImpl;
import org.springframework.test.context.junit4.SpringRunner;

import groovy.sql.Sql;

@RunWith(SpringRunner.class)
public class CoonectionServiceTest {

	ConnectioService connectioService=null;
	
	@Before
	public void setUp() {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("connections.yml").getFile());
		
	    connectioService = new ConnectionServiceImpl(file.getAbsolutePath());
	}
	
	@Test
	public void getConnections() {
		String[] conns = connectioService.getConnectionsNames();
		assertEquals(conns.length,4);
		assertEquals(conns[1], "DB Provedores");
	}
	
	@Test
	public void getConnection() {
		Sql sql = connectioService.getConnection("DB Provedores");
		assertNotNull(sql);
	}
	
	
	@Test
	public void getQuery() {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("query.sql").getFile());
		System.out.println(file.getAbsolutePath());
		
		String query = connectioService.getQuery(file.getAbsolutePath());
		assertEquals(query, "select * from tabla where id > 1000");
	}
	
	@Test
	public void getParams() {
		List<String> params = connectioService.getParams("select * from tabla where id > :id");
		assertEquals(params.size(), 1);
	}
	
	@Test
	public void getParams2() {
		List<String> params = connectioService.getParams("select * from tabla where id > :id and row= :row and other= :other");
		
		assertEquals(params.size(), 3);		
	}
	
}
