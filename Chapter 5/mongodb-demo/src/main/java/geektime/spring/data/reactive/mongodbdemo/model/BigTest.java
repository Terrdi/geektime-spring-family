package geektime.spring.data.reactive.mongodbdemo.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BigTest {
    private String test;

    private Integer port;

    private SmallTest small;

    public SmallTest getSmall() {
        return small;
    }
}
