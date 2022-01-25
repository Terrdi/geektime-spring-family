package geektime.spring.hello;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order
@Slf4j
public class ExitApplicationRunner implements ApplicationRunner, ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Autowired
    private ExitCodeGenerator exitCodeGenerator;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        int code = SpringApplication.exit(applicationContext, exitCodeGenerator);
        log.info("Exit with {}.", code);
        System.exit(code);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
