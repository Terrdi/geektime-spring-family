package geektime.spring.data.errorcodedemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.SQLExceptionSubclassTranslator;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ErrorCodeDemoApplication {
	@Component
	public static class CustomSQLExceptionTranslator extends SQLExceptionSubclassTranslator {
		private static List<Integer> errCodes = new ArrayList<>();

		static {
			errCodes.add(23001);
			errCodes.add(23005);
		}

		@Override
		@Nullable
		protected DataAccessException doTranslate(String task, String sql, SQLException ex) {
			if (errCodes.contains(ex.getErrorCode())) {
				return new CustomDuplicatedKeyException(buildMessage(task, sql, ex), ex);
			}
			return super.doTranslate(task, sql, ex);
		}
	}


	@Autowired
	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource, CustomSQLExceptionTranslator customSQLExceptionTranslator) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		return jdbcTemplate;
	}

	public static void main(String[] args) {
		SpringApplication.run(ErrorCodeDemoApplication.class, args);
	}
}

