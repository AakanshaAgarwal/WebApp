package com.codeplanet;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TestRestRepository {
	
    @Autowired
    JdbcTemplate jdbcTemplate;
    
	public Map getFirstApi() {
		
		
		
		return null;
	}

}
