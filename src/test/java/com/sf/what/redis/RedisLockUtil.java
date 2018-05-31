package com.sf.what.redis;
/**
 *redis分布式锁
 */
public final class RedisLockUtil { 
	/*
	 
	private static final int defaultExpire = 60; 
	private RedisLockUtil() {
		
	} 
	*//**
	 * 加锁方式一 
	 * @param key redis key 
	 * @param expire 过期时间，单位秒 
	 * @return true:加锁成功，false，加锁失败
	 *//* 
	public static boolean lock(String key, int expire) {
		RedisService redisService = SpringUtils.getBean(RedisService.class);
		//设置key 如果key不存在则设置成功，返回1 如果key已存在 则设置失败，返回0
		long status = redisService.setnx(key, "1"); 
		if(status == 1) {//表示获取锁成功
			//设置key 的超时时间，防止死锁 。
			redisService.expire(key, expire); 
			return true; 
		} 
		return false; 
	} 
	
	public static boolean lock(String key) { 
		return lock2(key, defaultExpire);
	} 
	*//**
	 * 加锁方式二 
	 * @param key redis key 
	 * @param expire 过期时间，单位秒 
	 * @return true:加锁成功，false，加锁失败 
	 *//* 
	public static boolean lock2(String key, int expire) { 
		RedisService redisService = SpringUtils.getBean(RedisService.class); 
		//计算key一个的失效时间
		long value = System.currentTimeMillis() + expire; 
		//设置key value为key的失效时间
		long status = redisService.setnx(key, String.valueOf(value)); 
		if(status == 1) {//设置成功，获取到锁
			return true; 
		}
		//没有获取成功
		//取得key的失效时间
		long oldExpireTime = Long.parseLong(redisService.get(key, "0"));
		//如果失效时间小于当前系统时间  则表示key超时 
		if(oldExpireTime < System.currentTimeMillis()) { 
			//计算新的超时时间
			long newExpireTime = System.currentTimeMillis() + expire;
			//getSet(key,value)返回当前key的值并设置新的key值
			long currentExpireTime = Long.parseLong(redisService.getSet(key, String.valueOf(newExpireTime))); 
			//如果当前值等于旧的失效时间，表示未被修改过，此时获取锁成功，否则表示已被别的请求获取，则获取锁失败。
			if(currentExpireTime == oldExpireTime) { 
				return true; 
			} 
		} 
		return false;
	} 
	
	public static void unLock1(String key) {
		RedisService redisService = SpringUtils.getBean(RedisService.class); 
		//使用方式一获取锁，在执行完任务后，直接删除key值
		redisService.del(key); 
	} 
	public static void unLock2(String key) {
		RedisService redisService = SpringUtils.getBean(RedisService.class); 
		//使用方式二获取锁，在执行完任务后，判断key是否超时，未超时，则删除key值
		long oldExpireTime = Long.parseLong(redisService.get(key, "0"));
		if(oldExpireTime > System.currentTimeMillis()) { 
			redisService.del(key); 
		} 
	} 
	
	*/
	
}