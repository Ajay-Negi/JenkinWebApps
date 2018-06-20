package com.dmi.dao.impl;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dmi.dao.IRedisDAO;
import com.dmi.dao.constant.Constants;

import redis.clients.jedis.Jedis;

@Repository
public class RedisDAO implements IRedisDAO {

	@Override
	public void save(String tableName, Map<String, String> hmValue) {
		// JEDIS AUTH
				Jedis jedis = new Jedis(Constants.REDIS_IP);
				jedis.auth(Constants.REDIS_AUTH);
				
				jedis.hmset(tableName, hmValue);

				// closing JEDIS client
				jedis.close();

	}

	@Override
	public void delete(String tableName, String key) {
		Jedis jedis = new Jedis(Constants.REDIS_IP);
		jedis.auth(Constants.REDIS_AUTH);
		
		jedis.hdel(tableName,key);

		// closing JEDIS client
		jedis.close();
		
	}
	


}
