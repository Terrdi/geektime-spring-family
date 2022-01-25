package geektime.spring.springbucks;

import geektime.spring.springbucks.model.DistributedLock;
import geektime.spring.springbucks.service.CoffeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@EnableTransactionManagement
@SpringBootApplication
@EnableJpaRepositories
@EnableCaching(proxyTargetClass = true)
public class SpringBucksApplication implements ApplicationRunner {
	@Autowired
	private CoffeeService coffeeService;

	@Autowired
	private DistributedLock lock;

	public static void main(String[] args) {
		SpringApplication.run(SpringBucksApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info("Count: {}", coffeeService.findAllCoffee().size());
		for (int i = 0; i < 5; i++) {
			log.info("Reading from cache.");
			coffeeService.findAllCoffee();
		}
		Thread.sleep(5_000);
		log.info("Reading after refresh.");
		coffeeService.findAllCoffee().forEach(c -> log.info("Coffee {}", c.getName()));

		for (int  i = 0;i < 10;i++) {
			log.info("Get Coffee {}", coffeeService.findOneCoffee("macchiato").orElse(null));
		}

		log.info("{} 尝试获取分布式锁", System.getProperty("application-id"));
		lock.lock();
		log.info("{} 获取到分布式锁", System.getProperty("application-id"));
		Thread.sleep(30_000);
		log.info("{} 尝试释放分布式锁", System.getProperty("application-id"));
		lock.unlock();
		log.info("{} 释放分布式锁", System.getProperty("application-id"));
	}

	@Bean
	public DefaultRedisScript<Long> redisScript() {
		DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
		redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("scripts/release_lock.lua")));
		redisScript.setResultType(Long.class);
		return redisScript;
	}
}

