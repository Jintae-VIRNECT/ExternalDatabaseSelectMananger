package com.virnect.extrernal.database.application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.virnect.extrernal.database.config.properties.Connection;
import com.virnect.extrernal.database.constnace.DatabaseType;
import com.virnect.extrernal.database.factory.DatabaseConnectionFactory;

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
@Slf4j
@Service
@RequiredArgsConstructor
public class DatabaseQueryService {

	private final DatabaseConnectionFactory databaseConnectionFactory;

	public List<String> getDatabases() {

		return databaseConnectionFactory.getDatabases();
	}

	public List<String> getTables(String connectionName) {
		Connection connectionInfo = databaseConnectionFactory.getConnectionInfo(connectionName);
		DataSource dataSource = databaseConnectionFactory.createDataSource(connectionInfo.getName());
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		String query = getTablesQuery(connectionInfo.getDatabaseType());

		return jdbcTemplate.queryForList(query, String.class);
	}

	private String getTablesQuery(DatabaseType databaseType) {
		switch (databaseType) {
			case MYSQL:
				return "SHOW TABLES";
			case ORACLE:
				return "SELECT table_name FROM user_tables";
			case MSSQL:
				return "SELECT s.name+'.'+t.name FROM sys.tables t INNER JOIN sys.schemas s ON t.schema_id = s.schema_id";
			default:
				throw new IllegalArgumentException("Unsupported database type: " + databaseType);
		}
	}

	public List<String> getColumns(String connectionName, String tableName) {
		Connection connectionInfo = databaseConnectionFactory.getConnectionInfo(connectionName);
		DataSource dataSource = databaseConnectionFactory.createDataSource(connectionName);

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		String query = getColumnsQuery(connectionInfo.getDatabaseType(), tableName);

		return jdbcTemplate.query(query, (rs, rowNum) -> getColumnValue(rs, connectionInfo.getDatabaseType()));
	}

	private String getColumnsQuery(DatabaseType databaseType, String tableName) {
		switch (databaseType) {
			case MYSQL:
				return "SHOW COLUMNS FROM " + tableName;
			case ORACLE:
				return "SELECT column_name FROM user_tab_columns WHERE table_name = '" + tableName.toUpperCase() + "'";
			case MSSQL:
				return
					"SELECT column_name = c.name FROM sys.columns c INNER JOIN sys.tables t ON c.object_id = t.object_id WHERE t.name = '"
						+ tableName + "'";
			default:
				throw new IllegalArgumentException("Unsupported database type: " + databaseType);
		}
	}

	private String getColumnValue(ResultSet rs, DatabaseType databaseType) throws SQLException {
		switch (databaseType) {
			case MYSQL:
				return rs.getString("Field");
			case ORACLE:
			case MSSQL:
				return rs.getString("column_name");
			default:
				throw new IllegalArgumentException("Unsupported database type: " + databaseType);
		}
	}

	public List<Map<String, Object>> selectData(
		String connectionName, String tableName, String column, String whereCondition
	) {
		DataSource dataSource = databaseConnectionFactory.createDataSource(connectionName);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		String sql = "SELECT " + column + " FROM " + connectionName + "." + tableName;

		if (StringUtils.isNotEmpty(whereCondition)) {
			sql = StringUtils.join(sql, " WHERE ", whereCondition, " = ?");
			return jdbcTemplate.queryForList(sql, whereCondition);
		}

		return jdbcTemplate.queryForList(sql);
	}

	public List<Map<String, Object>> selectDataByQuery(String connectionName, String query) {

		DataSource dataSource = databaseConnectionFactory.createDataSource(connectionName);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		return jdbcTemplate.queryForList(query);
	}
}
