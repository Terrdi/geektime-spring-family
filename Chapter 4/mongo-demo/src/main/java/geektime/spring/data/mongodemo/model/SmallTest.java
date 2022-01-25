package geektime.spring.data.mongodemo.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
public class SmallTest {
    @Value("bbb")
    @Field("name")
    private String name;

    private String value;
}
