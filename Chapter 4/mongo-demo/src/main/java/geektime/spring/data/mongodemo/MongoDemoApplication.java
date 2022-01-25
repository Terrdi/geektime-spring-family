package geektime.spring.data.mongodemo;

import geektime.spring.data.mongodemo.converter.MoneyReadConverter;
import geektime.spring.data.mongodemo.model.BigTest;
import geektime.spring.data.mongodemo.model.SmallTest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.Arrays;

@SpringBootApplication
@Slf4j
public class MongoDemoApplication implements ApplicationRunner {
	@Autowired
	private MongoTemplate mongoTemplate;

	public static void main(String[] args) {
		SpringApplication.run(MongoDemoApplication.class, args);
	}

	@Bean
	public MongoCustomConversions mongoCustomConversions() {
		return new MongoCustomConversions(
				Arrays.asList(new MoneyReadConverter()));
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
//		Coffee espresso = Coffee.builder()
//				.name("espresso")
//				.price(Money.of(CurrencyUnit.of("CNY"), 20.0))
//				.createTime(new Date())
//				.updateTime(new Date()).build();
//		Coffee saved = mongoTemplate.save(espresso);
//		log.info("Coffee {}", saved);
//
//		List<Coffee> list = mongoTemplate.find(
//				Query.query(Criteria.where("name").is("espresso")), Coffee.class);
//		log.info("Find {} Coffee", list.size());
//		list.forEach(c -> log.info("Coffee {}", c));
//
//		Thread.sleep(1000); // 为了看更新时间
//		UpdateResult result = mongoTemplate.updateFirst(query(where("name").is("espresso")),
//				new Update().set("price", Money.ofMajor(CurrencyUnit.of("CNY"), 30))
//						.currentDate("updateTime"),
//				Coffee.class);
//		log.info("Update Result: {}", result.getModifiedCount());
//		Coffee updateOne = mongoTemplate.findById(saved.getId(), Coffee.class);
//		log.info("Update Result: {}", updateOne);
//
//		mongoTemplate.remove(updateOne);

		SmallTest smallTest = new SmallTest();
		smallTest.setName("brbr");
		smallTest.setValue("dede");
		BigTest bigTest = new BigTest();
		bigTest.setPort(123);
		bigTest.setTest("i'm your father");
		bigTest.setSmall(smallTest);
		mongoTemplate.save(bigTest);
	}
}

