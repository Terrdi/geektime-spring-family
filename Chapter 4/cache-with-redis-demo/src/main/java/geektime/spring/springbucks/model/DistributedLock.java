package geektime.spring.springbucks.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 分布式锁
 * 使用redis实现
 *
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2022/1/25
 * @since 1.8
 **/
@Slf4j
@Component("lock")
public class DistributedLock implements Lock, InitializingBean {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private DefaultRedisScript<Long> redisScript;

    private String key = "release-key";

    private String uuid = UUID.randomUUID().toString();

    @Override
    public void lock() {
        boolean ret;
        do {
            ret = this.tryLock();
            if (!ret) {
                Thread.yield();
            }
        } while (!ret);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }

        this.lock();
    }

    @Override
    public boolean tryLock() {
        return Optional.ofNullable(redisTemplate.opsForValue()
                .setIfAbsent(key, uuid, Duration.ofMinutes(30))).orElse(Boolean.FALSE);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        long end = System.currentTimeMillis() + unit.toMillis(time);
        boolean ret;
        do {
            ret = this.tryLock();
            if (!ret) {
                Thread.yield();
            }
        } while (!ret && end <= System.currentTimeMillis());
        return ret;
    }

    @Override
    public void unlock() {
        if (Optional.ofNullable(redisTemplate.execute(this.redisScript, Collections.singletonList(this.key),
                this.uuid)).orElse(0L) > 0L) {
            log.info("成功释放锁");
        }
    }

    @Override
    public Condition newCondition() {
        throw new UnsupportedOperationException("Distributed lock doesn't support notify other process.");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.setProperty("application-id", this.uuid);
    }
}
