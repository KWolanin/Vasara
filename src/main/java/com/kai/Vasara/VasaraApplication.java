package com.kai.Vasara;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableCaching
public class VasaraApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(VasaraApplication.class).run(args);
	}

	@Bean
	public MessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public CacheManager cacheManager() {
		SimpleCacheManager cacheManager = new SimpleCacheManager();
		List<Cache> caches = new ArrayList<>();
		caches.add(new ConcurrentMapCache("chapterContent"));
		caches.add(new ConcurrentMapCache("userStoriesCache"));
		caches.add(new ConcurrentMapCache("storiesCache"));
		cacheManager.setCaches(caches);
		return cacheManager;
	}

}
