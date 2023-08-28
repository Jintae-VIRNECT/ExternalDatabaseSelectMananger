package com.virnect.extrernal.database.factory;

import java.util.List;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import com.virnect.extrernal.database.config.properties.Connection;
import com.virnect.extrernal.database.config.properties.DatabaseProperties;
import com.virnect.extrernal.database.constnace.DatabaseType;

/**
 * Project        : extrernal
 * DATE           : 2023-08-25
 * AUTHOR         : pc (Jintae Kim)
 * EMAIL          : jtkim@virnect.com
 * DESCRIPTION    :
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-25      pc          최초 생성
 */
@Component
@RequiredArgsConstructor
public class DatabaseConnectionFactory {

	private final DatabaseProperties databaseProperties;

	public List<String> getDatabases() {
		return databaseProperties.getConnections().stream()
			.map(Connection::getName)
			.collect(Collectors.toList());
	}

	public DataSource createDataSource(String name) {
		Connection connectionInfo = getConnectionInfo(name);

		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(getDriverClassName(connectionInfo.getDatabaseType()));
		dataSource.setUrl(connectionInfo.getUrl());
		dataSource.setUsername(connectionInfo.getUsername());
		dataSource.setPassword(connectionInfo.getPassword());

		return dataSource;
	}

	public Connection getConnectionInfo(String name) {
		return databaseProperties.getConnections().stream()
			.filter(c -> c.getName().equalsIgnoreCase(name))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("Connection not found: " + name));
	}

	private String getDriverClassName(DatabaseType databaseType) {
		switch (databaseType) {
			case MYSQL:
				return "com.mysql.cj.jdbc.Driver";
			case ORACLE:
				return "oracle.jdbc.driver.OracleDriver";
			case MSSQL:
				return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
			default:
				throw new IllegalArgumentException("Unsupported database type: " + databaseType);
		}
	}
}
