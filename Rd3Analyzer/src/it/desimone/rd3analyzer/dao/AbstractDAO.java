package it.desimone.rd3analyzer.dao;

import java.sql.Connection;

public class AbstractDAO {

	protected Connection connection;
	
	public AbstractDAO(Connection connection){
		this.connection = connection;
	}
}
