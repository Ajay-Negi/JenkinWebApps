package com.dmi.dao;

import java.util.Map;

public interface IRedisDAO {
	
	void save(String tableName, Map<String, String> hmValue );
	void delete(String tableName, String key); 


}
