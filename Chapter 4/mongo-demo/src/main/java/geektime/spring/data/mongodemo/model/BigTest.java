package geektime.spring.data.mongodemo.model;

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
