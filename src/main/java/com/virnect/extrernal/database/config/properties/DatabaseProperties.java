package com.virnect.extrernal.database.config.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

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
@Component
@ConfigurationProperties(prefix = "databases")
public class DatabaseProperties {
	private List<Connection> connections;



}
