package com.virnect.extrernal.database.config.properties;

import org.springframework.boot.context.properties.ConstructorBinding;

import lombok.Data;

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
@Data
@ConstructorBinding
public class Connection {
	private String name;
	private String username;
	private String password;
	private DatabaseType databaseType;
	private String url;
}
