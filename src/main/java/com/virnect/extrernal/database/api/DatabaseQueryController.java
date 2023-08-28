package com.virnect.extrernal.database.api;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.virnect.extrernal.database.application.DatabaseQueryService;

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
@RestController
@RequiredArgsConstructor
@RequestMapping("/database")
public class DatabaseQueryController {

	private final DatabaseQueryService databaseQueryService;

	@GetMapping("")
	public ResponseEntity<List<String>> getDatabase() {
		List<String> databases = databaseQueryService.getDatabases();
		return ResponseEntity.ok(databases);
	}

	@GetMapping("/tables")
	public ResponseEntity<List<String>> getTables(
		@RequestParam(value = "connectionName", required = true) String connectionName
	) {
		List<String> tables = databaseQueryService.getTables(connectionName);
		return ResponseEntity.ok(tables);
	}

	@GetMapping("/columns")
	public ResponseEntity<List<String>> getColumns(
		@RequestParam(value = "connectionName", required = true) String connectionName,
		@RequestParam(value = "tableName", required = true) String tableName
	) {
		List<String> columns = databaseQueryService.getColumns(connectionName, tableName);
		return ResponseEntity.ok(columns);
	}

	@GetMapping("/select")
	public ResponseEntity<List<Map<String, Object>>> selectData(
		@RequestParam String connectionName,
		@RequestParam String tableName,
		@RequestParam(required = false) String columns,
		@RequestParam(required = false) String whereCondition
	) {
		List<Map<String, Object>> data = databaseQueryService.selectData(connectionName, tableName, columns, whereCondition);
		return ResponseEntity.ok(data);
	}

	@GetMapping("/select/query")
	public ResponseEntity<List<Map<String, Object>>> selectDataByQuery(
		@RequestParam String connectionName,
		@RequestParam String query
	) {
		List<Map<String, Object>> data = databaseQueryService.selectDataByQuery(connectionName, query);
		return ResponseEntity.ok(data);
	}
}
